package com.eleven.casinobot.command;

import com.eleven.casinobot.command.commands.common.HelpCommand;
import com.eleven.casinobot.command.commands.common.PingCommand;
import com.eleven.casinobot.command.commands.common.SetPrefixCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<ICommand> commandList = new ArrayList<>();

    public CommandManager() {
        addCommand(new HelpCommand(this));
        addCommand(new SetPrefixCommand());
        addCommand(new PingCommand());
    }

    private void addCommand(ICommand command) {
        boolean isNameFound = this.commandList
                .stream()
                .anyMatch((it) -> it.getName().equalsIgnoreCase(command.getName()));

        // it's already existed command
        if (isNameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commandList.add(command);
    }

    public List<ICommand> getCommandList() {
        return commandList;
    }

    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand command : commandList) {
            if (command.getName().equals(searchLower) || command.getAliases().contains(searchLower)) {
                return command;
            }
        }

        return null;
    }

    public void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage()
                .getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand command = getCommand(invoke);

        if (command != null) {
            event.getChannel().sendTyping()
                    .queue();
            List<String> strings = Arrays.asList(split)
                    .subList(1, split.length);

            CommandContext ctx = new CommandContext(event, strings);
            command.handle(ctx);
        }
    }

}
