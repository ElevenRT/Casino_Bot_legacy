package com.eleven.casinobot.command.commands.music;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.listener.GuildMusicManager;
import com.eleven.casinobot.listener.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class RepeatCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel textChannel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (selfVoiceState == null || !selfVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("통화방 밖에서는 반복 기능을 수행할 수 없습니다.")
                    .queue();
            return;
        }

        final Member member = ctx.getMember();

        if (MusicMessageUtil.memberNotInVoice(textChannel, member)) {
            return;
        }
        if (MusicMessageUtil.memberNotInSameVoice(textChannel, selfMember, member)) {
            return;
        }

        final GuildMusicManager musicManager = PlayerManager
                .INSTANCE.getMusicManager(ctx.getGuild());
        final boolean newRepeating = !musicManager.trackScheduler.repeating;
        
        musicManager.trackScheduler.repeating = newRepeating;
        textChannel.sendMessageFormat("루프 **%s**", newRepeating ? "작동" : "해제")
                .queue();
    }

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String getHelp() {
        return "현재 노래를 반복합니다.\n" +
                "Usage: `<prefix>loop`";
    }
}
