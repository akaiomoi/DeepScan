package xyz.invalidexception.deepscan.event;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.update.GenericUserPresenceEvent;
import net.dv8tion.jda.api.events.user.update.GenericUserUpdateEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.invalidexception.deepscan.ScanBot;
import xyz.invalidexception.deepscan.api.ApiResponse;
import xyz.invalidexception.deepscan.api.DeepScanClient;
import xyz.invalidexception.deepscan.util.EmbedUtil;

public class PresenceHandler extends ListenerAdapter {
    private DeepScanClient client = new DeepScanClient();

    @Override
    public void onUserActivityStart(@NotNull UserActivityStartEvent event) {
        if(event.getUser().isBot()) return;

        for(Activity a : event.getMember().getActivities()) {
//            if(a.getType() == Activity.ActivityType.CUSTOM_STATUS) {

                System.out.println("Requesting review of \"" + a.getName() + "\"");

                ApiResponse parsedResponse = client.reviewMessage(a.getName());

                if(parsedResponse.detected) {
                    StringBuilder str = new StringBuilder();
                    str.append("*\"" + a.getName() + "\"*\n");
                    str.append("**Sent By:** " + event.getUser().getAsMention() + "\n\n");

                    for(String s : parsedResponse.detect.keySet()) {
                        if(parsedResponse.detect.get(s)) {
                            str.append("'").append(s).append("' - ").append( (Math.round(parsedResponse.category_scores.get(s) * 100.0) / 100.0 )* 100).append("% Certain.\n");
                        }
                    }
                    ScanBot.getJda().getTextChannelById(ScanBot.getLogsChannel()).sendMessage(EmbedUtil.buildEmbed("User Status Flagged!", str.toString(), event.getUser())).queue();
                }
//            }
        }
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        ApiResponse parsedResponse = client.reviewMessage(event.getUser().getName());

        if(parsedResponse.detected) {
            StringBuilder str = new StringBuilder();
            str.append("*\"" + event.getUser() + "\"*\n");

            for(String s : parsedResponse.detect.keySet()) {
                if(parsedResponse.detect.get(s)) {
                    str.append("'").append(s).append("' - ").append( (Math.round(parsedResponse.category_scores.get(s) * 100.0) / 100.0 )* 100).append("% Certain.\n");
                }
            }
            ScanBot.getJda().getTextChannelById(ScanBot.getLogsChannel()).sendMessage(EmbedUtil.buildEmbed("User Name Update Flagged!", str.toString(), event.getUser())).queue();
        }
    }

    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        ApiResponse parsedResponse = client.reviewMessage(event.getUser().getName());

        if(parsedResponse.detected) {
            StringBuilder str = new StringBuilder();
            str.append("*\"" + event.getUser() + "\"*\n");

            for(String s : parsedResponse.detect.keySet()) {
                if(parsedResponse.detect.get(s)) {
                    str.append("'").append(s).append("' - ").append( (Math.round(parsedResponse.category_scores.get(s) * 100.0) / 100.0 )* 100).append("% Certain.\n");
                }
            }
            ScanBot.getJda().getTextChannelById(ScanBot.getLogsChannel()).sendMessage(EmbedUtil.buildEmbed("User Nickname Flagged!", str.toString(), event.getUser())).queue();
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if(event.getUser().isBot()) return;

        for(Activity a : event.getMember().getActivities()) {
            if(a.getType() == Activity.ActivityType.CUSTOM_STATUS) {

                System.out.println("Requesting review of \"" + a.getName() + "\"");

                ApiResponse parsedResponse = client.reviewMessage(a.getName());

                if(parsedResponse.detected) {
                    StringBuilder str = new StringBuilder();
                    str.append("*\"" + a.getName() + "\"*\n");
                    str.append("**User:** " + event.getUser().getAsMention() + "\n\n");

                    for(String s : parsedResponse.detect.keySet()) {
                        if(parsedResponse.detect.get(s)) {
                            str.append("'").append(s).append("' - ").append( (Math.round(parsedResponse.category_scores.get(s) * 100.0) / 100.0 )* 100).append("% Certain.\n");
                        }
                    }
                    ScanBot.getJda().getTextChannelById(ScanBot.getLogsChannel()).sendMessage(EmbedUtil.buildEmbed("User Status Flagged!", str.toString(), event.getUser())).queue();
                }
            }
        }

        ApiResponse parsedResponse = client.reviewMessage(event.getUser().getName());

        if(parsedResponse.detected) {
            StringBuilder str = new StringBuilder();
            str.append("*\"" + event.getUser() + "\"*\n");

            for(String s : parsedResponse.detect.keySet()) {
                if(parsedResponse.detect.get(s)) {
                    str.append("'").append(s).append("' - ").append( (Math.round(parsedResponse.category_scores.get(s) * 100.0) / 100.0 )* 100).append("% Certain.\n");
                }
            }
            ScanBot.getJda().getTextChannelById(ScanBot.getLogsChannel()).sendMessage(EmbedUtil.buildEmbed("User Name Flagged!", str.toString(), event.getUser())).queue();
        }
    }
}
