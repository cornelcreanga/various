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
        config.setJdbcUrl("jdbc:mysql://localhost:3306/petitions?useUnicode=true&characterEncoding=UTF-8");
        config.setUsername("root");
        config.setPassword("root");

        ds = new HikariDataSource(config);
    }

    public static Connection get(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

}
