package com.eleven.casinobot.database;

import com.eleven.casinobot.config.PrefixConfig;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseQuery {

    public void update(long guildId, String string, String Table, String colum, String where) {
        PrefixConfig.PREFIXS.put(guildId, string);

        try (final PreparedStatement preparedStatement = BotDatabase.getConnection()
                .prepareStatement("UPDATE " + Table +
                        " SET " + colum + " = ? " +
                        "where " + where + " = ?")) {
            preparedStatement.setString(1, string);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
