package com.eleven.casinobot.command.commands.music;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.lavaplayer.GuildMusicManager;
import com.eleven.casinobot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class NowPlayingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel textChannel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();

        GuildVoiceState selfVoiceState = selfMember.getVoiceState();
        if (selfVoiceState == null || !selfVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("통화방에 입장하지 않았기에 명령이 잘못되었습니다")
                    .queue();
            return;
        }

        final Member member = ctx.getMember();

        if (MusicMessageUtil.memberNotInVoice(textChannel, member)) {
            return;
        }

        if (MusicMessageUtil.memberNotExistedVoice(textChannel, member)) {
            return;
        }

        if (MusicMessageUtil.memberNotInSameVoice(textChannel, selfMember, member)) {
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.INSTANCE
                .getMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;
        final AudioTrack track = player.getPlayingTrack();

        if (track == null) {
            textChannel.sendMessage("현재 재생되고 있는 곡이 없습니다.")
                    .queue();
            return;
        }

        final AudioTrackInfo trackInfo = track.getInfo();
        textChannel.sendMessageFormat("`%s`가 현재 재생 중입니다. (링크: <%s>)",
                        trackInfo.title, trackInfo.uri)
                .queue();
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getHelp() {
        return "현재 재생되고 있는 곡을 표시합니다.\n" +
                "Usage: `<prefix>nowplaying`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("np");
    }
}
