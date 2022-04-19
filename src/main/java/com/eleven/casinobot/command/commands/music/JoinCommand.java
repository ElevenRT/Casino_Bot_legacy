package com.eleven.casinobot.command.commands.music;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel textChannel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();

        if (MusicMessageUtil.alreadyInVoice(textChannel, selfMember)) {
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberGuildVoiceState = member.getVoiceState();

        if (memberGuildVoiceState == null || !memberGuildVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("통화방에 먼저 입장하신 후, 명령어를 사용해주세요.")
                    .queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberVoiceChannel = memberGuildVoiceState.getChannel();

        audioManager.openAudioConnection(memberVoiceChannel);
        if (memberVoiceChannel != null) {
            textChannel.sendMessageFormat("`\uD83D\uDD0A`, %s에 연결하였습니다.",
                    memberVoiceChannel.getName())
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "봇을 통화방으로 입장시킵니다.\n" +
                "Usage: `<prefix>join`";
    }
}
