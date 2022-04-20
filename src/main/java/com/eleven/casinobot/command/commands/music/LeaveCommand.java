package com.eleven.casinobot.command.commands.music;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import com.eleven.casinobot.lavaplayer.GuildMusicManager;
import com.eleven.casinobot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements ICommand {

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

        final Guild guild = ctx.getGuild();
        final GuildMusicManager musicManager = PlayerManager.INSTANCE
                .getMusicManager(guild);

        musicManager.trackScheduler.repeating = false;
        musicManager.trackScheduler.audioTracks.clear();
        musicManager.player.stopTrack();

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
        textChannel.sendMessage("통화방을 나갔습니다.").queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "통화방에서 나갑니다.\n" +
                "Usage: `<prefix>leave`";
    }
}
