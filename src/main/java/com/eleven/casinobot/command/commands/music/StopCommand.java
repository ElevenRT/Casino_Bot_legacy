package com.eleven.casinobot.command.commands.music;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.listener.GuildMusicManager;
import com.eleven.casinobot.listener.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class StopCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel textChannel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (selfVoiceState == null || !selfVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("통화방 밖에서는 정지할 수 없습니다.").queue();
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

        musicManager.trackScheduler.player.stopTrack();
        musicManager.trackScheduler.audioTracks.clear();

        textChannel.sendMessage("The Player has been stopped and the queue has been cleared!").queue();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "현재 재생 중인 곡과 모든 곡을 멈춥니다.\n" +
                "Usage: `<prefix>stop`";
    }

}
