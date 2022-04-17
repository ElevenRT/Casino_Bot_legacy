package com.eleven.casinobot.database;

import com.eleven.casinobot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BotDatabase {

    private static final Logger log = LoggerFactory.getLogger(BotDatabase.class);

    static {
        Statement statement;

        try {
            statement = getConnection().createStatement();
            String defaultPrefix = BotConfig.getPrefix();
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS MEMBER (" +
                            "id BIGINT PRIMARY KEY, " +
                            "money BIGINT NOT NULL DEFAULT 0, " +
                            "fortune VARCHAR(20) NOT NULL DEFAULT 'NONE'" +
                            ");"
            );
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS GUILD (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "guild_id VARCHAR(20) NOT NULL, " +
                            "prefix VARCHAR(255) NOT NULL DEFAULT '" + defaultPrefix + "'" +
                            ");"
            );
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS MUSIC (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                            "guild_id VARCHAR(20) NOT NULL, " +
                            "music VARCHAR(255)" +
                            ");"
            );

            log.info("initialized mysql connected!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = BotConfig.getURL();
        String name = BotConfig.getUsername();
        String password = BotConfig.getPassword();

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, name, password);
    }

}
