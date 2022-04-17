package com.eleven.casinobot;

import com.eleven.casinobot.config.BotConfig;
import com.eleven.casinobot.database.BotDatabase;
import com.eleven.casinobot.listener.BotListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class CasinoBotApplication {

    public static void main(String[] args) throws LoginException, InterruptedException, SQLException, ClassNotFoundException {
        BotDatabase.getConnection();
        
        JDA jda = JDABuilder.createDefault(BotConfig.getToken())
                .addEventListeners(new BotListener())
                .build();
        jda.awaitReady();
    }

}
