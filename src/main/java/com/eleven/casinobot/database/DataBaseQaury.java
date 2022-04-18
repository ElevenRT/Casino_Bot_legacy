package com.eleven.casinobot.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseQaury {
    public void update(long guildId, String string, String Table, String colum, String where) {

        try (final PreparedStatement preparedStatement = BotDatabase.getConnection()
                .prepareStatement("UPDATE " + Table + " " +
                        " SET " + colum + " " + " = ? " +
                        "where " + where + " " + " = ?")) {

            preparedStatement.setString(1,string);
            preparedStatement.setString(2,String.valueOf(guildId));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
