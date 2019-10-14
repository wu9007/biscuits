package org.hv.biscuits.domain.process;

import org.hv.biscuits.spine.model.Process;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wujianchuan
 */
@Component
@Order(5)
public class NodeLauncher implements CommandLineRunner {
    private final List<Node> nodeList;

    public NodeLauncher(@Nullable List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public void run(String... args) throws Exception {

        if (nodeList != null) {
            Map<String, List<Node>> nodesMapper = nodeList.parallelStream().collect(Collectors.groupingBy(node -> {
                State state = node.getClass().getAnnotation(State.class);
                return state.group();
            }));

            ContextFactory contextFactory = ContextFactory.getInstance();
            nodesMapper.entrySet().parallelStream()
                    .forEach(entry -> {
                        List<Node> nodeList = entry.getValue();
                        Map<String, Node> nodeMapper = new HashMap<>();
                        for (Node node : nodeList) {
                            nodeMapper.put(node.getIdentify(), node);
                        }
                        contextFactory.putNodeMapper(entry.getKey(), nodeMapper);
                    });

            // 从持久化数据中获取所有流程中的节点，并进行恢复
            Session session = SessionFactory.getSession("biscuits");
            session.open();
            List<Process> processList = session.list(Process.class);
            session.close();
            for (Process process : processList) {
                Context context = contextFactory.buildProcessContext(process.getProcessId(), process.getDataUuid(), process.getCustomizedProcesses().split(","));
                context.setEnable(true);
                context.setCurrentNode(context.getNodeByIdentify(process.getCurrentNodeId()));
            }
        }
    }
}
