package com.eleven.casinobot.command.commands.music;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.listener.GuildMusicManager;
import com.eleven.casinobot.listener.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel textChannel = ctx.getChannel();
        final GuildMusicManager musicManager = PlayerManager
                .INSTANCE.getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> audioTracks = musicManager.trackScheduler.audioTracks;

        if (audioTracks.isEmpty()) {
            textChannel.sendMessage("음악 큐가 비었습니다.")
                    .queue();
            return;
        }

        final int trackCount = Math.min(audioTracks.size(), 20);
        final List<AudioTrack> tracks = new LinkedList<>(audioTracks);
        final MessageAction messageAction = textChannel.sendMessage("**현재 음악 큐**\n");

        for (int i = 0; i < trackCount; i++) {
            final AudioTrack track = tracks.get(i);
            final AudioTrackInfo trackInfo = track.getInfo();

            messageAction.append("#")
                    .append(String.valueOf(i + 1))
                    .append("`")
                    .append(trackInfo.title)
                    .append(" by ")
                    .append("` [`")
                    .append(format(track.getDuration()))
                    .append("`]\n");
        }

        if (audioTracks.size() > trackCount) {
            messageAction.append(", ")
                    .append(String.valueOf(audioTracks.size() - trackCount))
                    .append("` 더...`");
        }

        messageAction.queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "음악 큐를 보여줍니다.\n" +
                "Usage: `<prefix>queue`";
    }

    private String format(long duration) {
        final long hour = duration / TimeUnit.HOURS.toMillis(1);
        final long minutes = duration / TimeUnit.MINUTES.toMillis(1);
        final long seconds = duration % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hour, minutes, seconds);
    }
}
