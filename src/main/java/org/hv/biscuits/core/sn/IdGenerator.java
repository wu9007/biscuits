package org.hv.biscuits.core.sn;

import org.hv.biscuits.core.session.ActiveSessionCenter;
import org.hv.biscuits.function.BiscuitsSupplier;
import org.hv.pocket.exception.SessionException;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.model.MapperFactory;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import static org.hv.biscuits.core.sn.ResetRule.DAY;
import static org.hv.biscuits.core.sn.ResetRule.MONTH;
import static org.hv.biscuits.core.sn.ResetRule.YEAR;

/**
 * 单号生成器
 *
 * @author wujianchuan 2020/11/2 17:32
 */
public class IdGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerator.class);
    private static final IdGenerator INSTANCE = new IdGenerator();
    private final Map<String/* pre code */, AtomicLong/* max serial number */> maxSnMap = new HashMap<>(60);
    private final Map<String/* pre code */, Long/* thread id */> lockMap = new HashMap<>(60);
    private final Map<String/* pre code */, LocalDate/* latest date */> latestDateMap = new HashMap<>(60);
    private static final int MAX_RETRY_TIMES = 6;

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * 生成单号
     *
     * @param sessionName   数据库会话名称
     * @param clazz         持久化映射类 类 类型
     * @param fieldName     单号字段
     * @param preCode       前缀
     * @param dateFormatter 时间格式
     * @param serialLength  序号长度
     * @param resetRule     清零规则
     * @param strongSerial  是否强连续（是：阻塞，否：非阻塞）
     * @return 单号
     */
    public String generate(String sessionName, Class<? extends AbstractEntity> clazz, String fieldName, String preCode, String dateFormatter, int serialLength, String resetRule, Boolean strongSerial) throws Exception {
        LocalDate now = LocalDate.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern(dateFormatter));
        BiscuitsSupplier<AtomicLong> maxSnSupplier = () -> {
            Object todayMaxCode = null;
            // NOTE: 如果外层方法开启了数据库会话则直接使用，否则根据 sessionName 自行开启。
            Session parentSession = ActiveSessionCenter.getCurrentSession();
            String tableName = MapperFactory.getTableName(clazz.getName());
            String columnName = MapperFactory.getRepositoryColumnName(clazz.getName(), fieldName);
            String sql = "SELECT " + columnName + " AS " + fieldName + " FROM " + tableName + " WHERE " + columnName + " LIKE :MATCH_STR";
            if (parentSession == null) {
                Session newSession = SessionFactory.getSession(sessionName);
                if (newSession == null) {
                    throw new SessionException("请正确开启数据库会话");
                }
                newSession.open();
                List<String> idList = newSession.createSQLQuery(sql).setParameter("MATCH_STR", preCode + dateStr + "_____").list();
                if (!idList.isEmpty()) {
                    todayMaxCode = idList.stream().max(Comparator.comparing((item) -> Long.parseLong(item.replace(preCode + dateStr, "")))).get();
                }
                newSession.close();
            } else {
                List<String> idList = parentSession.createSQLQuery(sql).setParameter("MATCH_STR", preCode + dateStr + "_____").list();
                if (!idList.isEmpty()) {
                    todayMaxCode = idList.stream().max(Comparator.comparing((item) -> Long.parseLong(item.replace(preCode + dateStr, "")))).get();
                }
            }
            latestDateMap.put(preCode, now);
            return todayMaxCode == null ? new AtomicLong(0) : new AtomicLong(Long.parseLong(todayMaxCode.toString().replace(preCode + dateStr, "")));
        };
        return (strongSerial == null || strongSerial) ? this.generateStrongSerial(preCode, dateStr, serialLength, resetRule, maxSnSupplier) : this.generateWeakSerial(preCode, dateStr, serialLength, resetRule, maxSnSupplier);
    }

    /**
     * 单号提交
     *
     * @param preCode 前缀
     */
    public void commit(String preCode) {
        boolean lock = lockMap.get(preCode) != null;
        if (lock) {
            lockMap.remove(preCode);
            LOGGER.debug("<<<<<<<<<<<<<<<< Thread - {} unlock - {} >>>>>>>>>>>>>>>>>", Thread.currentThread().getId(), preCode);
            LOGGER.info("单号生成器提交事务，当前最大序号 - {}", maxSnMap.get(preCode));
        }
    }

    /**
     * 单号回滚
     *
     * @param preCode 前缀
     */
    public void rollback(String preCode) {
        boolean lock = lockMap.get(preCode) != null;
        if (lock) {
            maxSnMap.get(preCode).decrementAndGet();
            lockMap.remove(preCode);
            LOGGER.debug("<<<<<<<<<<<<<<<< Thread - {} unlock - {} >>>>>>>>>>>>>>>>>", Thread.currentThread().getId(), preCode);
            LOGGER.info("单号生成器回滚事务，当前最大序号 - {}", maxSnMap.get(preCode));
        }
    }

    /**
     * 生成强连续单号生成
     *
     * @param preCode       前缀
     * @param dateStr       日期格式
     * @param serialLength  序号长度
     * @param resetRule     清零规则
     * @param maxSnSupplier 获取最大序号
     * @return 单号
     */
    private String generateStrongSerial(String preCode, String dateStr, int serialLength, String resetRule, BiscuitsSupplier<AtomicLong> maxSnSupplier) throws Exception {
        // NOTE: 获取锁（首次获取||多次获取）；使用 synchronized (preCode) 会大量消耗内存故使用 Map。
        boolean lock = lockMap.putIfAbsent(preCode, Thread.currentThread().getId()) == null || lockMap.get(preCode).equals(Thread.currentThread().getId());
        if (lock) {
            LOGGER.debug("<<<<<<<<<<<<<<<< Thread - {} lock - {} >>>>>>>>>>>>>>>>>", Thread.currentThread().getId(), preCode);
            // NOTE: 先从堆中获取当太难最大序列号如果没有再从数据库查询。
            AtomicLong maxSn = this.maxSnMap.get(preCode);
            if (maxSn == null) {
                maxSn = maxSnSupplier.get();
                this.maxSnMap.put(preCode, maxSn);
                LOGGER.debug("================= Thread - {} 初始化 {} 序列号 {} =================", Thread.currentThread().getId(), preCode, maxSn);
            } else {
                LocalDate now = LocalDate.now();
                LocalDate latestDate = latestDateMap.get(preCode);
                switch (resetRule) {
                    case DAY:
                        break;
                    case MONTH:
                        now = LocalDate.of(now.getYear(), now.getMonth(), 1);
                        latestDate = LocalDate.of(latestDate.getYear(), latestDate.getMonth(), 1);
                        break;
                    case YEAR:
                        now = LocalDate.of(now.getYear(), Month.JANUARY, 1);
                        latestDate = LocalDate.of(latestDate.getYear(), Month.JANUARY, 1);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("不支持的规则 - %s", resetRule));
                }
                if (now.isAfter(latestDate)) {
                    maxSn = new AtomicLong(0);
                    latestDateMap.put(preCode, LocalDate.now());
                }
            }
            return preCode + dateStr + String.format("%0" + serialLength + "d", maxSn.incrementAndGet());
        } else {
            long sleepMillis = 2;
            LOGGER.info("线程 - {} 第 {} 次获取前缀为 {} 最大序号失败，将要在等待 {}ms 后重新获取。", Thread.currentThread().getId(), 1, preCode, sleepMillis);
            for (int i = 1; i < MAX_RETRY_TIMES; i++) {
                Thread.sleep(sleepMillis);
                if (lockMap.putIfAbsent(preCode, Thread.currentThread().getId()) == null) {
                    return this.generateStrongSerial(preCode, dateStr, serialLength, resetRule, maxSnSupplier);
                }
                sleepMillis = sleepMillis * 2;
                LOGGER.info("线程 - {} 第 {} 次获取前缀为 {} 最大序号失败，将要在等待 {}ms 后重新获取。", Thread.currentThread().getId(), i + 1, preCode, sleepMillis);
            }
            LOGGER.error("线程 - {} 次获取前缀为 {} 最大序号重试次数超过 {} 次", Thread.currentThread().getId(), preCode, MAX_RETRY_TIMES);
            throw new TimeoutException(String.format("重试次数超过%s次", MAX_RETRY_TIMES));
        }
    }

    /**
     * 生成弱连续单号生成
     *
     * @param preCode       前缀
     * @param dateStr       日期格式
     * @param serialLength  序号长度
     * @param resetRule     清零规则
     * @param maxSnSupplier 获取最大序号
     * @return 单号
     */
    private String generateWeakSerial(String preCode, String dateStr, int serialLength, String resetRule, BiscuitsSupplier<AtomicLong> maxSnSupplier) throws Exception {
        AtomicLong maxSn = this.maxSnMap.get(preCode);
        if (maxSn == null) {
            // NOTE: 获取锁 使用 synchronized (preCode) 会大量消耗内存故使用 Map。
            boolean lock = lockMap.putIfAbsent(preCode, Thread.currentThread().getId()) == null;
            if (lock) {
                LOGGER.debug("<<<<<<<<<<<<<<<< Thread - {} lock - {} >>>>>>>>>>>>>>>>>", Thread.currentThread().getId(), preCode);
                maxSn = this.maxSnMap.get(preCode);
                if (maxSn == null) {
                    maxSn = maxSnSupplier.get();
                    this.maxSnMap.put(preCode, maxSn);
                    LOGGER.debug("================= Thread - {} 初始化 {} 序列号 {} =================", Thread.currentThread().getId(), preCode, maxSn);
                }
                lockMap.remove(preCode);
                LOGGER.debug("<<<<<<<<<<<<<<<< Thread - {} unlock - {} >>>>>>>>>>>>>>>>>", Thread.currentThread().getId(), preCode);
                return preCode + dateStr + String.format("%0" + serialLength + "d", maxSn.incrementAndGet());
            } else {
                long sleepMillis = 2;
                LOGGER.info("线程 - {} 第 {} 次获取前缀为 {} 最大序号失败，将要在等待 {}ms 后重新获取。", Thread.currentThread().getId(), 1, preCode, sleepMillis);
                for (int i = 1; i < MAX_RETRY_TIMES; i++) {
                    Thread.sleep(sleepMillis);
                    if (this.maxSnMap.get(preCode) != null) {
                        return this.generateWeakSerial(preCode, dateStr, serialLength, resetRule, maxSnSupplier);
                    }
                    sleepMillis = sleepMillis * 2;
                    LOGGER.info("线程 - {} 第 {} 次获取前缀为 {} 最大序号失败，将要在等待 {}ms 后重新获取。", Thread.currentThread().getId(), i + 1, preCode, sleepMillis);
                }
                LOGGER.error("线程 - {} 次获取前缀为 {} 最大序号重试次数超过 {} 次", Thread.currentThread().getId(), preCode, MAX_RETRY_TIMES);
                throw new TimeoutException(String.format("重试次数超过%s次", MAX_RETRY_TIMES));
            }
        }
        LocalDate now = LocalDate.now();
        LocalDate latestDate = latestDateMap.get(preCode);
        switch (resetRule) {
            case DAY:
                break;
            case MONTH:
                now = LocalDate.of(now.getYear(), now.getMonth(), 0);
                latestDate = LocalDate.of(latestDate.getYear(), latestDate.getMonth(), 0);
                break;
            case YEAR:
                now = LocalDate.of(now.getYear(), Month.JANUARY, 0);
                latestDate = LocalDate.of(latestDate.getYear(), Month.JANUARY, 0);
                break;
            default:
                throw new IllegalArgumentException(String.format("不支持的规则 - %s", resetRule));
        }
        if (now.isAfter(latestDate)) {
            if (!maxSn.compareAndSet(maxSn.get(), 0)) {
                return this.generateWeakSerial(preCode, dateStr, serialLength, resetRule, maxSnSupplier);
            }
            latestDateMap.put(preCode, LocalDate.now());
        }
        return preCode + dateStr + String.format("%0" + serialLength + "d", maxSn.incrementAndGet());
    }
}
