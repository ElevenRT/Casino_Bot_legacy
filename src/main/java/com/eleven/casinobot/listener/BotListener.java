package com.eleven.casinobot.listener;

import com.eleven.casinobot.command.CommandManager;
import com.eleven.casinobot.config.BotConfig;
import com.eleven.casinobot.config.PrefixConfig;
import com.eleven.casinobot.database.BotDatabase;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BotListener extends ListenerAdapter {

    private final Logger log = LoggerFactory.getLogger(BotListener.class);

    private final CommandManager commandManager;

    public BotListener() {
        this.commandManager = new CommandManager();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        log.info("{} is Ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final long guildId = event.getGuild().getIdLong();
        String prefix = PrefixConfig.PREFIXS.computeIfAbsent(guildId, this::getPrefix);
        String msg = event.getMessage().getContentRaw();

        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        if (msg.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(BotConfig.getDEV())) {
            log.info("Shutting Down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
            return;
        }

        if (msg.startsWith(prefix)) {
            commandManager.handle(event, prefix);
        }
    }

    private String getPrefix(long guildId) {
        try (final PreparedStatement preparedStatement = BotDatabase.getConnection()
                .prepareStatement("SELECT prefix FROM guild WHERE guild_id = ?")) {
            preparedStatement.setString(1, String.valueOf(guildId));
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }
            try (final PreparedStatement insertStatement = BotDatabase.getConnection()
                    .prepareStatement("INSERT INTO GUILD(guild_id) VALUES (?)")) {
                insertStatement.setString(1, String.valueOf(guildId));
                insertStatement.execute();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return BotConfig.getPrefix();
    }

}
