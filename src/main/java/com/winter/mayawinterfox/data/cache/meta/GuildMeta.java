package com.winter.mayawinterfox.data.cache.meta;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.http.Feed;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

public class GuildMeta {

	private final Guild guild;
	private final Set<String> prefixes;
	private final Set<Snowflake> autoroles;
	private final Set<Feed> feeds;
	private final TextChannel welcomeChannel;
	private final String language;
	private final String welcome;
	private final String pm;
	private final boolean levelupNotifications;
	private final boolean premium;
	private final boolean newGuild;
	private final boolean permissions;
	private final boolean welcomeEnabled;
	private final boolean welcomeEmbed;

	public GuildMeta(Guild guild,
	                 Set<String> prefixes,
	                 Set<Snowflake> autoroles,
	                 Set<Feed> feeds,
	                 TextChannel welcomeChannel,
	                 String language,
	                 String welcome,
	                 String pm,
	                 boolean levelupNotifications,
	                 boolean premium,
	                 boolean newGuild,
	                 boolean permissions,
	                 boolean welcomeEnabled,
	                 boolean welcomeEmbed) {
		this.guild = guild;
		this.prefixes = prefixes;
		this.autoroles = autoroles;
		this.feeds = feeds;
		this.welcomeChannel = welcomeChannel;
		this.language = language;
		this.welcome = welcome;
		this.pm = pm;
		this.levelupNotifications = levelupNotifications;
		this.premium = premium;
		this.newGuild = newGuild;
		this.permissions = permissions;
		this.welcomeEnabled = welcomeEnabled;
		this.welcomeEmbed = welcomeEmbed;
	}

	public Guild getGuild() {
		return guild;
	}

	public Set<String> getPrefixes() {
		if (prefixes.isEmpty()) {
			return Collections.singleton(Main.config.get(Main.ConfigValue.PREFIX));
		} else {
			Set<String> prefix = prefixes;
			prefix.add(Main.config.get(Main.ConfigValue.PREFIX));
			return prefix;
		}
	}

	public Mono<GuildMeta> addPrefix(String prefix) {
		return Database.set("INSERT IGNORE INTO prefixes (id, prefix) VALUES (?, ?);", guild, prefix)
				.then(Caches.getGuild(guild));
	}

	public Mono<GuildMeta> removePrefix(String prefix) {
		return Database.set("DELETE FROM prefixes WHERE id=? AND prefix=?", guild, prefix)
				.then(Caches.getGuild(guild));
	}

	public Set<Snowflake> getAutoroles() {
		return autoroles;
	}

	public TextChannel getWelcomeChannel() {
		return welcomeChannel;
	}

	public Mono<GuildMeta> setWelcomeChannel(TextChannel channel) {
		return Database.set("UPDATE guild SET welcomeChannel=? WHERE id=?;", channel.getId().asLong(), channel.getGuildId().asLong())
				.then(Caches.getGuild(guild));
	}
	
	public String getLanguage() {
		return language;
	}

	public Mono<GuildMeta> setLanguage(String language) {
		return Database.set("UPDATE guild SET language=? WHERE id=?;", language, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public String getWelcome() {
		return welcome;
	}

	public String getWelcome(User user) {
		return welcome.replace("[user]", user.getUsername())
				.replace("[mention]", user.getMention())
				.replace("[server]", guild.getName());
	}

	public Mono<GuildMeta> setWelcome(String welcome) {
		return Database.set("UPDATE guild SET welcome=? WHERE id=?;", welcome, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public String getPm() {
		return pm;
	}

	public Mono<GuildMeta> setPm(String pm) {
		return Database.set("UPDATE guild SET pm=? WHERE id=?;", pm, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public boolean isLevelupNotifications() {
		return levelupNotifications;
	}

	public Mono<GuildMeta> setLevelupNotifications(boolean levelupNotifications) {
		return Database.set("UPDATE guild SET lvlup=? WHERE id=?;", levelupNotifications, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public boolean isPremium() {
		return premium;
	}

	public Mono<GuildMeta> setPremium(boolean premium) {
		return Database.set("UPDATE guild SET premium=? WHERE id=?;", premium, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public boolean isNewGuild() {
		return newGuild;
	}

	public Mono<GuildMeta> setNewGuild(boolean newGuild) {
		return Database.set("UPDATE guild SET newguild=? WHERE id=?;", newGuild, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public boolean hasCustomPermissions() {
		return permissions;
	}

	public Mono<GuildMeta> setCustomPermissions(boolean permissions) {
		return Database.set("UPDATE guild SET permission=? WHERE id=?", permissions, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public boolean isWelcomeEnabled() {
		return welcomeEnabled;
	}

	public Mono<GuildMeta> toggleWelcomeEnabled() {
		return Database.set("UPDATE guild SET welcomeEnabled=? WHERE id=?", !welcomeEnabled, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public boolean isWelcomeEmbed() {
		return welcomeEmbed;
	}

	public Mono<GuildMeta> toggleWelcomeEmbed() {
		return Database.set("UPDATE guild SET welcomeEmbed=? WHERE id=?", !welcomeEmbed, guild.getId().asLong())
				.then(Caches.getGuild(guild));
	}

	public Set<Feed> getFeeds() {
		return feeds;
	}

	/**
	 * Subscribe a guild to a feed
	 * @param url The url to subscribe to
	 * @return The guild object
	 */
	public Mono<GuildMeta> addFeed(String url) {
		return Database.set("INSERT IGNORE INTO rss (feed, guild) VALUES (?, ?)", url, guild)
				.then(Caches.getGuild(guild));
	}

	/**
	 * Unsubscribe a guild from a feed
	 * @param url The url to unsubscribe from
	 * @return The guild object
	 */
	public Mono<GuildMeta> removeFeed(String url) {
		return Database.set("DELETE FROM rss WHERE feed=? AND guild=?", url, guild)
				.then(Caches.getGuild(guild));
	}
}