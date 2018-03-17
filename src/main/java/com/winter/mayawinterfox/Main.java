package com.winter.mayawinterfox;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.item.ItemProvider;
import com.winter.mayawinterfox.data.music.GuildMusicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Main {

	public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	public static Map<ConfigValue, String> config = new EnumMap<>(ConfigValue.class);
	private static IDiscordClient client = null;

	public static AudioPlayerManager playerManager;
	public static Map<IGuild, GuildMusicManager> musicManagers;

	/**
	 * Main method, guh!
	 * @param args Args taken in from the run parameters
	 * @throws IOException Upon failure to read the config
	 */
	public static void main(String[] args) throws IOException {
		InputStream configProperties = Main.class.getResourceAsStream("/config.properties");
		Properties p = new Properties();
		p.load(configProperties);
		configProperties.close();
		p.keySet().forEach(k -> config.put(ConfigValue.from(k.toString()), p.getProperty(k.toString())));

		ClientBuilder b = new ClientBuilder();
		b.setMaxReconnectAttempts(0);
		if(config.get(ConfigValue.DEBUG).equalsIgnoreCase("false")) {
			b.withToken(config.get(ConfigValue.TOKEN));
		} else if (config.get(ConfigValue.DEBUG).equalsIgnoreCase("true")) {
			b.withToken(config.get(ConfigValue.DEBUG_TOKEN));
		} else {
			LOGGER.error("Invalid debug value, it must be 'true' or 'false'");
			System.exit(1);
		}
		b.withRecommendedShardCount();

		b.registerListener(new Commands());
		b.registerListener(new EventListener());

		Database.connect();
		if (!Database.setup()) {
			LOGGER.error("Failed to setup database for bot use!");
			System.exit(1);
		}
		LOGGER.info("Successfully set up database for bot usage");

		ItemProvider.loadFoods();
		ItemProvider.loadItems();

		//Runtime.getRuntime().addShutdownHook(new Thread(() -> {
		//	Caches.getManager().removeCache("guilds");
		//	Caches.getManager().removeCache("users");
		//	Caches.getManager().close();
		//}));

		musicManagers = new HashMap<>();
		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);

		client = b.login();
	}

	/**
	 * Get the discord client
	 * @return Returns the IDiscordClient instance
	 */
	public static IDiscordClient getClient() {
		return client;
	}

	/**
	 * Values that are inside of the config and directly read from the config
	 */
	public enum ConfigValue {
		DEBUG("debug"),
		TOKEN("token"),
		DEBUG_TOKEN("debug-token"),
		ERROR_CHANNEL("error-channel"),
		DB_PASS("db-password"),
		DB_POOL_SIZE("db-pool-size"),
		GLOBALS("globals"),
		BOTS_PW_TOKEN("bots-pw-token"),
		BOTS_ORG_TOKEN("bots-org-token"),
		MAL_CREDENTIALS("mal-credentials"),
		MASHAPE_KEY("mashape-key"),
		YOUTUBE_KEY("youtube-key"),
		IMGUR_CID("imgur-cid"),
		MAX_PREFIXES("max-prefixes"),
		MAX_PREFIXES_PREMIUM("max-prefixes-premium"),
		MAX_AUTOROLES("max-autoroles"),
		MAX_AUTOROLES_PREMIUM("max-autoroles-premium"),
		MAX_SONG_QUEUE("max-song-queue"),
		MAX_SONG_QUEUE_PREMIUM("max-song-queue-premium"),
		PREFIX("prefix");

		private final String name;

		/**
		 * Get a config value for a key
		 * @param key The config key to getGuild
		 * @return The config value for the given key
		 */
		public static ConfigValue from(String key) {
			return Arrays.stream(ConfigValue.values()).filter(v -> v.getName().equals(key)).findFirst().orElseThrow(() -> new IllegalArgumentException("Not a config value!"));
		}

		ConfigValue(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
