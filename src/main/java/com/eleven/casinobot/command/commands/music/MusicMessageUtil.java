package com.eleven.casinobot.command.commands.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class MusicMessageUtil {

    private MusicMessageUtil() {}

    public static boolean memberNotInVoice(TextChannel textChannel, Member member) {
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null || !memberVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("통화방에 먼저 입장하신 후, 명령어를 사용해주세요.").queue();
            return true;
        }
        return false;
    }

    public static boolean memberNotExistedVoice(TextChannel textChannel, Member member) {
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null || memberVoiceState.getChannel() == null) {
            textChannel.sendMessage("통화방을 찾지 못하였습니다.")
                    .queue();
            return true;
        }
        return false;
    }

    public static boolean memberNotInSameVoice(TextChannel textChannel, Member selfMember, Member member) {
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (memberVoiceState == null || selfVoiceState == null) {
            textChannel.sendMessage("동일한 통화방에 있는 상태에서 명령어를 사용해주세요.")
                    .queue();
            return true;
        }

        if (memberVoiceState.getChannel() == null || selfVoiceState.getChannel() == null) {
            textChannel.sendMessage("동일한 통화방에 있는 상태에서 명령어를 사용해주세요.")
                    .queue();
            return true;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            textChannel.sendMessage("동일한 통화방에 있는 상태에서 명령어를 사용해주세요.")
                    .queue();
            return true;
        }
        return false;
    }

    public static boolean alreadyInVoice(TextChannel textChannel, Member member) {
        final GuildVoiceState selfVoiceState = member.getVoiceState();
        if (selfVoiceState == null || selfVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("이미 통화방에 들어왔습니다.")
                    .queue();
            return true;
        }
        return false;
    }

}
