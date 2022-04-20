package com.eleven.casinobot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer player;
    public final BlockingQueue<AudioTrack> audioTracks;
    public boolean repeating = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.audioTracks = new LinkedBlockingQueue<>();
    }

    public void addTrack(AudioTrack audioTrack, long guildId) {
        if (!player.startTrack(audioTrack, true)) {
            audioTracks.offer(audioTrack);
        }
    }

    public void nextTrack() {
        player.startTrack(audioTracks.poll(), false);
    }


    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        super.onTrackEnd(player, track, endReason);
    }
}
