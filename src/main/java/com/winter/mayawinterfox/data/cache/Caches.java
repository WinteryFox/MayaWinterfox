package com.winter.mayawinterfox.data.cache;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.Row;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.http.Feed;
import com.winter.mayawinterfox.data.item.ItemProvider;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.util.stream.Collectors;

public class Caches {

	/**
	 * Get a guild from the cache, if the guild is not already in the cache the guild will be put into it
	 *
	 * @param guild The guild implementation to getGuild the meta for
	 * @return The guild meta for the guild implementation
	 */
	public static Mono<GuildMeta> getGuild(@NotNull Guild guild) {
		final var settings = Database.get("SELECT * FROM guild WHERE id=?", guild.getId().asLong())
				.next()
				.map(Row::getColumns);
		final var prefixes = Database.get("SELECT * FROM prefixes WHERE id=?", guild.getId().asLong())
				.map(v -> (String) v.get("prefix"))
				.collect(Collectors.toSet());
		final var autoroles = Database.get("SELECT * FROM autoroles WHERE id=?", guild.getId().asLong())
				.map(v -> Snowflake.of((Long) v.get("role")))
				.collect(Collectors.toSet());
		final var feeds = Database.get("SELECT * FROM rss WHERE guild=?", guild)
				.map(v -> {
					return new Feed((String) v.get("feed"), Snowflake.of((Long) v.get("channel")), Snowflake.of((Long) v.get("guild")));
				})
				.collect(Collectors.toSet());
		return Mono.zip(settings, prefixes, autoroles, feeds)
				.zipWhen(v -> guild.getChannelById(Snowflake.of((Long) v.getT1().get("welcomeChannel"))).ofType(TextChannel.class))
				.map(v -> {
					var o = v.getT1();
					var s = o.getT1();
					var p = o.getT2();
					var r = o.getT3();
					var f = o.getT4();
					var c = v.getT2();
					return new GuildMeta(guild,
							p,
							r,
							f,
							c,
							(String) s.get("language"),
							(String) s.get("welcome"),
							(String) s.get("pm"),
							(boolean) s.get("lvlup"),
							(boolean) s.get("premium"),
							(boolean) s.get("newguild"),
							(boolean) s.get("permission"),
							(boolean) s.get("welcomeEnabled"),
							(boolean) s.get("welcomeEmbed"));
				});
	}

	/**
	 * Get a user from the cache, if the user is not already in the cache the user will be put into it
	 *
	 * @param user The user implementation to getGuild the meta for
	 * @return The user meta for the user implementation
	 */
	public static Mono<UserMeta> getUser(User user) {
		return Database.get("SELECT * FROM user WHERE id=?;", user.getId().asLong())
				.next()
				.map(Row::getColumns)
				.map(v -> new UserMeta(user,
						(String) v.get("description"),
						(int) v.get("level"),
						(int) v.get("xp"),
						(int) v.get("maxxp"),
						(long) v.get("totalxp"),
						(int) v.get("coins"),
						(int) v.get("gems"),
						ItemProvider.getItemById((int) v.get("background")),
						(boolean) v.get("notifications"),
						(boolean) v.get("premium"),
						(Date) v.get("premiumexpiry")));
	}
}