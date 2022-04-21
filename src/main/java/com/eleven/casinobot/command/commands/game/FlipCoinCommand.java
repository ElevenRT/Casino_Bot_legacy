package com.eleven.casinobot.command.commands.game;

import com.eleven.casinobot.command.CommandContext;
import com.eleven.casinobot.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FlipCoinCommand implements ICommand {

    public static String head = "1️⃣";
    public static String tail = "2️⃣";

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        if(FlipCoin.server.contains(channel.getIdLong())){
            channel.sendMessage("이미 게임을 진행 중입니다.").queue();
            return;
        }

        FlipCoin.server.add(channel.getGuild().getIdLong());
        channel.sendMessage("10초가 지나기 전에 배팅 해 주세요.\n")
                .append("앞면에 배팅하시려면 눌러주세요 : ")
                .append(head)
                .append("\n뒷면에 배팅하시려면 눌러주세요 : ")
                .append(tail)
                .queue(msg -> {
                    msg.addReaction(head).queue();
                    msg.addReaction(tail).queue();
                    msg.delete().queueAfter(10L, TimeUnit.SECONDS);
                    String coinResult = Math.random() > 0.5 ? "앞면" : "뒷면";
                    channel.sendMessage(coinResult).queueAfter(10L,TimeUnit.SECONDS);
                    Boolean coin = coinResult.equals("앞면");

                    try{
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (long GameUser : FlipCoin.user.keySet()){
                        if (FlipCoin.user.get(GameUser) == coin) {
                            channel.sendMessageFormat("<@%s>, 축하드립니다. 맞추셨습니다.", GameUser).queue();
                        }
                        else {
                            channel.sendMessageFormat("<@%s>, 안타깝습니다. 틀리셨습니다.", GameUser).queue();
                        }
                    }
                    FlipCoin.server.remove(channel.getGuild().getIdLong());
                    FlipCoin.user.clear();
                });


    }

    @Override
    public String getName() {
        return "동전";
    }

    @Override
    public String getHelp() {
        return "동전을 굴립니다.\n" +
                "이모티콘을 눌러 앞면 혹은 뒷면을 선택할 수 있습니다.";
    }


    public static class FlipCoin{
        public static ConcurrentHashMap<Long, Boolean> user = new ConcurrentHashMap<>();
        public static List<Long> server = Collections.synchronizedList(new ArrayList<>());

    }
}