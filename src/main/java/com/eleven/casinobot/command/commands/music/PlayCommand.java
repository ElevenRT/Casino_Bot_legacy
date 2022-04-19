package com.eleven.casinobot.command.commands.music;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.config.BotConfig;
import com.eleven.casinobot.listener.GuildMusicManager;
import com.eleven.casinobot.listener.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

public class PlayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel textChannel = ctx.getChannel();
        final GuildMusicManager musicManager = PlayerManager
                .INSTANCE.getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> audioQueue = musicManager
                .trackScheduler.audioTracks;

        if (ctx.getStrings().isEmpty()) {
            textChannel.sendMessage("명령어 사용법: `" + BotConfig.getPrefix() + "play <youtube link>`").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        assert selfVoiceState != null;
        if (!selfVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("통화방 밖에서는 노래를 재생할 수 없습니다.")
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

        if (audioQueue.size() >= 20) {
            textChannel.sendMessage("음악은 최대 20개까지만 저장할 수 있습니다.\n" +
                    "재생되고 있는 곡이 끝난 후 다시 시도해주세요.").queue();
            return;
        }

        String link = String.join(" ", ctx.getStrings());

        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.INSTANCE
                .loadAndPlay(textChannel, link);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "음악을 재생합니다.\nUsage: `<prefix>play <youtube link>`";
    }

    private boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        }catch (MalformedURLException e) {
            return false;
        }
    }
}
