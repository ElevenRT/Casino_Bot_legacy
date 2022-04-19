package com.eleven.casinobot.command.commands.music;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.listener.GuildMusicManager;
import com.eleven.casinobot.listener.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel textChannel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (selfVoiceState == null || !selfVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("통화방 밖에서는 건너뛰기를 할 수 없습니다.").queue();
            return;
        }

        final Member member = ctx.getMember();

        if (MusicMessageUtil.memberNotInVoice(textChannel, member)) {
            return;
        }
        if (MusicMessageUtil.memberNotInSameVoice(textChannel, selfMember, member)) {
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.INSTANCE.getMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            textChannel.sendMessage("현재 재생 중인 곡이 없습니다.").queue();
            return;
        }

        musicManager.trackScheduler.nextTrack();
        textChannel.sendMessage("현재 재생 중인 곡을 건너뛰기했습니다.").queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "현재 재생 중인 곡을 건너뛰기합니다.\n" +
                "Usage: `<prefix>skip`";
    }

}
