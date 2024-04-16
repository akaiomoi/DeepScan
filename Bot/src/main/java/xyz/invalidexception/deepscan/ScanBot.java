package xyz.invalidexception.deepscan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import xyz.invalidexception.deepscan.event.MessageHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import xyz.invalidexception.deepscan.event.PresenceHandler;

public class ScanBot {

	private static String TOKEN;
    @Getter private static Gson gson;
	@Getter private static JDA jda;
	@Getter  private static long logsChannel;


	public static void main(String[] args) {
		gson = new GsonBuilder().create();

		if(args.length > 1) {
			TOKEN = args[0];
			logsChannel = Long.parseLong(args[1]);
		} else {
			System.out.println("INVALID ARGUMENTS!");
			System.out.println("java -jar DeepScan.jar <token> <channel id>");
			System.exit(0);
		}

		try {

            jda = JDABuilder.create(TOKEN, GatewayIntent.GUILD_MESSAGES)
                    .disableCache(CacheFlag.ACTIVITY,
                            CacheFlag.VOICE_STATE,
                            CacheFlag.EMOTE,
                            CacheFlag.CLIENT_STATUS)
                    .addEventListeners(new MessageHandler(), new PresenceHandler())
                    .build()
                    .awaitReady();

        } catch(Exception e) {
			System.exit(-1);
		}


	}

}
