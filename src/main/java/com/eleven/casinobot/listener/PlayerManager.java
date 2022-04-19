package com.eleven.casinobot.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    public static final PlayerManager INSTANCE = new PlayerManager();

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, String trackUrl) {
        final GuildMusicManager musicManager = getMusicManager(textChannel.getGuild());

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.trackScheduler.addTrack(track, textChannel.getGuild().getIdLong());

                textChannel.sendMessage("`업로더: `")
                        .append(track.getInfo().author)
                        .append("`의 `")
                        .append(track.getInfo().title)
                        .append("`")
                        .append("를 큐에 추가했습니다.")
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> trackList = playlist.getTracks();

                for (final AudioTrack track : trackList) {
                    musicManager.trackScheduler.addTrack(track, textChannel.getGuild().getIdLong());

                    textChannel.sendMessage("업로더: `")
                            .append(track.getInfo().author)
                            .append("`의 `")
                            .append(track.getInfo().title)
                            .append("`")
                            .append("를 큐에 추가했습니다.")
                            .queue();
                    break;
                }
            }

            @Override
            public void noMatches() {
                // 정보를 찾지 못하였을 경우
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                // 정보를 찾았으나, 가져오지 못하였을 경우
            }
        });
    }

}
