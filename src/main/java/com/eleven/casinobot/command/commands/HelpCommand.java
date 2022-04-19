package com.eleven.casinobot.command.commands;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.CommandManager;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.config.PrefixConfig;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public class HelpCommand implements ICommand {

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    CommandManager manager;
    @Override
    public void handle(CommandContext ctx) {
        List<String> strings = ctx.getStrings();
        TextChannel channel = ctx.getChannel();

        if (strings.isEmpty()) { //.도움말 시 보여줄 것들
            StringBuilder stringBuilder = new StringBuilder();
            String prefix = PrefixConfig.PREFIXS.get(channel.getGuild().getIdLong());

            stringBuilder.append("명령어 리스트\n");

            manager.getCommandList().stream().map(ICommand::getName).forEach(
                    it -> stringBuilder.append("`").append(prefix).append(it).append("`\n")
            );
            channel.sendMessage(stringBuilder.toString()).queue();
        }

        String search = strings.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("`" + search + "을/를 찾지 못했습니다.`").queue();
            return;
        }

        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "도움말";
    }

    @Override
    public String getHelp() {
        return "명령어 리스트 입니다.\n"
                + "Usage: `" + PrefixConfig.PREFIXS + " [command]`";
    }
}
