package com.eleven.casinobot.command.commands;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.config.PrefixConfig;
import com.eleven.casinobot.database.BotDatabase;
import com.eleven.casinobot.database.DataBaseQaury;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SetPrefix implements ICommand {

    DataBaseQaury qaury = new DataBaseQaury();
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> chat = ctx.getStrings();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("이 명령어를 사용하려면 관리자 권한이 필요합니다!").queue();
            return;
        }

        if (chat.isEmpty()) {
            channel.sendMessage("Prefix를 지정하지 않았습니다!").queue();
            return;
        }

        final String prefix = String.join("",chat);
        qaury.update(channel.getGuild().getIdLong(),prefix,"GUILD","prefix","guild_id");

    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for this server\n" +
                "Usage: `" + PrefixConfig.PREFIXS + "setprefix <prefix>`";
    }

}
