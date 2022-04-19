package com.eleven.casinobot.listener;

import com.eleven.casinobot.lavaplayer.AudioPlayerSendHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {

    public final AudioPlayer player;
    public final TrackScheduler trackScheduler;

    private final AudioPlayerSendHandler audioPlayerSendHandler;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.player = manager.createPlayer();
        this.trackScheduler = new TrackScheduler(this.player);
        this.player.addListener(this.trackScheduler);
        this.audioPlayerSendHandler = new AudioPlayerSendHandler(this.player);
    }

    public AudioPlayerSendHandler getHandler() {
        return audioPlayerSendHandler;
    }

}
