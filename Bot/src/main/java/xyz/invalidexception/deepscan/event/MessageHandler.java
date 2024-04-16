package xyz.invalidexception.deepscan.event;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import xyz.invalidexception.deepscan.ScanBot;
import xyz.invalidexception.deepscan.api.ApiResponse;
import xyz.invalidexception.deepscan.api.DeepScanClient;
import xyz.invalidexception.deepscan.util.EmbedUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageHandler extends ListenerAdapter {
    private DeepScanClient client = new DeepScanClient();
    @SneakyThrows
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.isWebhookMessage() || event.getAuthor().isBot()) return;

        System.out.println("Requesting review of \"" + event.getMessage().getContentStripped() + "\"");

        ApiResponse parsedResponse = client.reviewMessage(event.getMessage().getContentStripped());

        if(parsedResponse.detected) {
            StringBuilder str = new StringBuilder();
            str.append("*\"" + event.getMessage().getContentStripped() + "\"*\n");
            str.append("**Sent By:** " + event.getAuthor().getAsMention() + "\n\n");

            for(String s : parsedResponse.detect.keySet()) {
                if(parsedResponse.detect.get(s)) {
                    str.append("'").append(s).append("' - ").append( (Math.round(parsedResponse.category_scores.get(s) * 100.0) / 100.0 )* 100).append("% Certain.\n");
                }
            }
            ScanBot.getJda().getTextChannelById(ScanBot.getLogsChannel()).sendMessage(EmbedUtil.buildEmbed("Message Flagged!", str.toString(), event.getAuthor())).queue();
        }
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        if(event.getAuthor().isBot()) return;

        System.out.println("Requesting review of \"" + event.getMessage().getContentStripped() + "\"");

        ApiResponse parsedResponse = client.reviewMessage(event.getMessage().getContentStripped());

        if(parsedResponse.detected) {
            StringBuilder str = new StringBuilder();
            str.append("*\"" + event.getMessage().getContentStripped() + "\"*\n");
            str.append("**Sent By:** " + event.getAuthor().getAsMention() + "\n\n");

            for(String s : parsedResponse.detect.keySet()) {
                if(parsedResponse.detect.get(s)) {
                    str.append("'").append(s).append("' - ").append( (Math.round(parsedResponse.category_scores.get(s) * 100.0) / 100.0 )* 100).append("% Certain.\n");
                }
            }
            ScanBot.getJda().getTextChannelById(ScanBot.getLogsChannel()).sendMessage(EmbedUtil.buildEmbed("Updated Message Flagged!", str.toString(), event.getAuthor())).queue();
        }
    }
}
