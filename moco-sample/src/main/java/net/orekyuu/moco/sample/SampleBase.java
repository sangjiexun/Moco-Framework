package net.orekyuu.moco.sample;

import com.mysql.cj.jdbc.MysqlDataSource;
import net.orekyuu.moco.core.ConnectionManager;
import net.orekyuu.moco.core.DataSourceType;

import java.sql.Connection;
import java.sql.SQLException;

public class SampleBase {
    public static void setup() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/moco_sample");
        dataSource.setUser("moco");
        dataSource.setPassword("moco");
        ConnectionManager.initialize(dataSource, DataSourceType.MYSQL);
    }

    public interface TransactionTask {
        void run() throws SQLException;
    }

    public static void transaction(TransactionTask task) {
        Connection connection = ConnectionManager.getConnection();
        try {
            connection.setAutoCommit(false);
            task.run();
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
