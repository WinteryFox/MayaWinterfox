package com.winter.mayawinterfox.data.cache;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.Row;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.item.ItemProvider;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Caches {

	//private static final Logger LOGGER = LoggerFactory.getLogger(Caches.class);

	//private static CacheManager manager = CacheManagerBuilder.newCacheManagerBuilder()
	//		.withCache("guilds",
	//				CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, GuildMeta.class, ResourcePoolsBuilder.heap(1000)))
	//		.withCache("users",
	//				CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, UserMeta.class, ResourcePoolsBuilder.heap(5000))).build(true);
	//private static Cache<Long, GuildMeta> GUILDS = manager.getCache("guilds", Long.class, GuildMeta.class);
	//private static Cache<Long, UserMeta> USERS = manager.getCache("users", Long.class, UserMeta.class);

	/**
	 * Get a guild from the cache, if the guild is not already in the cache the guild will be put into it
	 *
	 * @param guild The guild implementation to getGuild the meta for
	 * @return The guild meta for the guild implementation
	 */
	public static GuildMeta getGuild(Guild guild) {
		/*if (!GUILDS.containsKey(guild.getLongID())) {
			Database.set("INSERT IGNORE INTO guild (id) VALUES (?);", guild.getLongID());
			Map<String, List<Object>> settings = Database.getGuild("SELECT * FROM guild WHERE id=?", guild.getLongID());
			Map<String, List<Object>> prefixes = Database.getGuild("SELECT * FROM prefixes WHERE id=?", guild.getLongID());
			Map<String, List<Object>> autoroles = Database.getGuild("SELECT * FROM autoroles WHERE id=?", guild.getLongID());
			GUILDS.putIfAbsent(guild.getLongID(), new GuildMeta(guild.getShard(), guild.getName(), guild.getLongID(), guild.getIcon(), guild.getOwnerLongID(), guild.getAFKChannel() != null ? guild.getAFKChannel().getLongID() : 0L, guild.getAFKTimeout(), guild.getRegion().getID(), guild.getVerificationLevel().ordinal(), prefixes.getOrDefault("prefix", Collections.emptyList()).stream().map(v -> (String) v).collect(Collectors.toSet()), autoroles.getOrDefault("role", Collections.emptyList()).stream().map(v -> (Long) v).collect(Collectors.toSet()), (String) settings.getGuild("language").getGuild(0), (String) settings.getGuild("welcome").getGuild(0), (String) settings.getGuild("pm").getGuild(0), (boolean) settings.getGuild("lvlup").getGuild(0), (boolean) settings.getGuild("premium").getGuild(0), (boolean) settings.getGuild("newguild").getGuild(0)));
			LOGGER.trace("Put guild meta for guild " + guild.getName() + " in the cache.");
		}
		return GUILDS.getGuild(guild.getLongID());*/
		Database.set("INSERT IGNORE INTO guild (id) VALUES (?);", guild.getId().asLong());
		List<Row> settings = Database.get("SELECT * FROM guild WHERE id=?", guild.getId().asLong());
		List<Row> prefixes = Database.get("SELECT * FROM prefixes WHERE id=?", guild.getId().asLong());
		List<Row> autoroles = Database.get("SELECT * FROM autoroles WHERE id=?", guild.getId().asLong());
		return new GuildMeta(guild,
				prefixes.stream().map(v -> (String) v.get("prefix")).collect(Collectors.toSet()),
				autoroles.stream().map(v -> (Long) v.get("role")).collect(Collectors.toSet()),
				guild.getChannelById(Snowflake.of((Long) settings.get(0).get("welcomeChannel"))).block(),
				(String) settings.get(0).get("language"),
				(String) settings.get(0).get("welcome"),
				(String) settings.get(0).get("pm"),
				(boolean) settings.get(0).get("lvlup"),
				(boolean) settings.get(0).get("premium"),
				(boolean) settings.get(0).get("newguild"),
				(boolean) settings.get(0).get("permission"),
				(boolean) settings.get(0).get("welcomeEnabled"),
				(boolean) settings.get(0).get("welcomeEmbed"));
		
		/*Set<String> prefixes,
		Set<Long> autoroles,
		IChannel welcomeChannel,
		String language,
		String welcome,
		String pm,
		boolean levelupNotifications,
		boolean premium,
		boolean newGuild,
		boolean permissions,
		boolean welcomeEnabled,
		boolean welcomeEmbed*/
	}

	/**
	 * Get a user from the cache, if the user is not already in the cache the user will be put into it
	 *
	 * @param user The user implementation to getGuild the meta for
	 * @return The user meta for the user implementation
	 */
	public static UserMeta getUser(User user) {
		/*if (!USERS.containsKey(user.getLongID())) {
			Database.set("INSERT IGNORE INTO user (id) VALUES (?);", user.getLongID());
			HashMap<String, List<Object>> result = Database.getGuild("SELECT * FROM user WHERE id=?;", user.getLongID());
			USERS.putIfAbsent(user.getLongID(), new UserMeta(user.getShard(), user.getName(), user.getLongID(), user.getDiscriminator(), user.getAvatar(), user.getPresence(), user.isBot(), (String) result.getGuild("description").getGuild(0), (int) result.getGuild("level").getGuild(0), (int) result.getGuild("xp").getGuild(0), (int) result.getGuild("maxxp").getGuild(0), (long) result.getGuild("totalxp").getGuild(0), (int) result.getGuild("coins").getGuild(0), (int) result.getGuild("gems").getGuild(0), ItemProvider.getItemById((int) result.getGuild("background").getGuild(0)), (boolean) result.getGuild("notifications").getGuild(0), (boolean) result.getGuild("premium").getGuild(0), (Date) result.getGuild("premiumexpiry").getGuild(0)));
			LOGGER.trace("Put user meta for user " + user.getName() + " in the cache.");
		}
		return USERS.getGuild(user.getLongID());*/
		Database.set("INSERT IGNORE INTO user (id) VALUES (?);", user.getId().asLong());
		List<Row> result = Database.get("SELECT * FROM user WHERE id=?;", user.getId().asLong());
		return new UserMeta(user,
				(String) result.get(0).get("description"),
				(int) result.get(0).get("level"),
				(int) result.get(0).get("xp"),
				(int) result.get(0).get("maxxp"),
				(long) result.get(0).get("totalxp"),
				(int) result.get(0).get("coins"),
				(int) result.get(0).get("gems"),
				ItemProvider.getItemById((int) result.get(0).get("background")),
				(boolean) result.get(0).get("notifications"),
				(boolean) result.get(0).get("premium"),
				(Date) result.get(0).get("premiumexpiry"));
	}

	/**
	 * Get the guild cache
	 * @return The guild cache
	 */
	//public static Cache<Long, GuildMeta> getGuildCache() {
	//	return GUILDS;
	//}

	/**
	 * Get the user cache
	 * @return The user cache
	 */
	//public static Cache<Long, UserMeta> getUserCache() {
	//	return USERS;
	//}

	//public static CacheManager getManager() {
	//	return manager;
	//}
}