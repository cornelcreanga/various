package scraper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import scraper.dao.SQLRuntimeException;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {

    static HikariDataSource ds;
    static{
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://172.17.0.2:3306/petitions?useUnicode=true&characterEncoding=UTF-8");
        config.setUsername("root");
        config.setPassword("root");

        ds = new HikariDataSource(config);
    }

    public static Connection get(){
        try {
            Connection c = ds.getConnection();
            c.setAutoCommit(false);
            return c;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

}
