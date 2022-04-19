package com.eleven.casinobot.command.commands.common;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                        .sendMessageFormat("유저와 서버간의 핑: %sms, 봇과 서버간의 핑: %sms", ping, jda.getGatewayPing())
                        .queue()
        );
        final User self = ctx.getMessage().getAuthor();
        final TextChannel channel = ctx.getChannel();
        channel.sendMessageFormat("<@%s>님의 핑입니다.", self.getId()).queue();
    }

    @Override
    public String getName() {
        return "핑";
    }

    @Override
    public String getHelp() {
        return "봇과 서버간의 핑과 유저와 서버간의 핑을 알려줍니다.";
    }
}
