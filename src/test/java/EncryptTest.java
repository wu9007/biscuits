import org.hv.Application;
import org.hv.pocket.constant.EncryptType;
import org.hv.pocket.flib.DecryptFunctionLib;
import org.hv.pocket.flib.EncryptFunctionLib;
import org.hv.pocket.utils.EncryptUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

/**
 * @author wujianchuan 2019/1/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EncryptTest {
    @Test
    public void test1() throws SQLException {
        String key = "sward18713839007";
        String columnValue = EncryptFunctionLib.getEncryptFunction(EncryptType.DES).apply("C-001", key);
        System.out.println(columnValue);
        String code = DecryptFunctionLib.getDecryptFunction(EncryptType.DES).apply(columnValue, key);
        System.out.println(code);
    }

    @Test
    public void test2() throws SQLException {
        String key = "sward18713839007";
        String columnValue = EncryptFunctionLib.getEncryptFunction(EncryptType.SM4_CEB).apply("root", key);
        System.out.println(columnValue);
        String code = DecryptFunctionLib.getDecryptFunction(EncryptType.SM4_CEB).apply(columnValue, key);
        System.out.println(code);
    }

    @Test
    public void test3() throws SQLException {
        String key = "sward18713839007";
        String columnValue = EncryptFunctionLib.getEncryptFunction(EncryptType.SM4_CBC).apply("hello word", key);
        System.out.println(columnValue);
        String code = DecryptFunctionLib.getDecryptFunction(EncryptType.SM4_CBC).apply(columnValue, key);
        System.out.println(code);
    }

    @Test
    public void test4() {
        String columnValue = EncryptUtil.encrypt(EncryptType.SM4_CBC, "tsxdtsxd");
        System.out.println(columnValue);
        String code = EncryptUtil.decrypt(EncryptType.SM4_CBC, columnValue);
        System.out.println(code);
    }
}
