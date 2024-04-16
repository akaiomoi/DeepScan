package xyz.invalidexception.deepscan.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedUtil {
    private static EmbedBuilder eb = new EmbedBuilder();
    private static Color embedColor = new Color(0,128,255);

    public static MessageEmbed buildEmbed(String title, String content) {
        eb.setColor(embedColor);
        eb.setTitle(title);
        eb.setDescription(content);
        eb.build();
        MessageEmbed embed = eb.build();
        eb.clear();
        return embed;
    }

    public static MessageEmbed buildEmbed(String title, String content, User user) {
        eb.setColor(embedColor);
        eb.setThumbnail(user.getAvatarUrl());
        eb.setTitle(title);
        eb.setDescription(content);
        eb.setFooter("DeepScan Demo");
        eb.build();
        MessageEmbed embed = eb.build();
        eb.clear();
        return embed;
    }
}
