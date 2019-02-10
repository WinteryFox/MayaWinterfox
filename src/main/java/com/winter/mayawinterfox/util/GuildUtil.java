package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Flux;

import java.util.List;

public class GuildUtil {

	public static TextChannel getFirstChannel(Guild guild) {
		if (guild.getSystemChannel().block().getEffectivePermissions(guild.getEveryoneRole().block().getId()).block().contains(Permission.SEND_MESSAGES))
			return guild.getSystemChannel().block();

		for (GuildChannel channel : guild.getChannels().collectList().block()) {
			if (!(channel instanceof TextChannel))
				continue;

			if (channel.getGuild().block().getEveryoneRole().block().getPermissions().contains(Permission.SEND_MESSAGES))
				return (TextChannel) channel;
		}
		return null;
	}

	public static boolean isPremium(Member user) {
		return Caches.getGuild(user.getGuild().block()).block().isPremium() || Caches.getUser(user).block().isPremium();
	}

	/**
	 * Assign a role to a user
	 * @param user The user to assign the role to
	 * @param role The role to assign
	 */
	public static void addRole(Member user, Role role) {
		user.addRole(role.getId()).block();
	}

	/**
	 * Remove a role from a user
	 * @param user The user to remove the role from
	 * @param role The role to remove
	 */
	public static void removeRole(Member user, Role role) {
		user.removeRole(role.getId()).block();
	}

	/**
	 * Bulk delete messages
	 * @param channel The channel to bulk delete from
	 * @param messages The messages to bulk delete
	 */
	public static void bulkDelete(TextChannel channel, List<Snowflake> messages) {
		channel.bulkDelete(Flux.fromIterable(messages)).blockFirst();
	}

	/**
	 * Checks if the guild has space left in the song queue
	 *
	 * @param user  The user to check for
	 * @return True if there is still space and false if there is not
	 */
	public static boolean hasQueueSpaceLeft(Member user) {
		GuildMeta g = Caches.getGuild(user.getGuild().block()).block();
		UserMeta u = Caches.getUser(user).block();
		//GuildMusicManager m = MusicUtils.getGuildMusicManager(user.getGuild().block());
		//return g.isPremium() || u.isPremium() ? m.getScheduler().getQueueSize() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_SONG_QUEUE_PREMIUM)) : m.getScheduler().getQueueSize() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_SONG_QUEUE));
		return true;
	}

	/**
	 * Checks if the guild has space left in the song queue
	 *
	 * @param g The guild to check for
	 * @param u The user to check for
	 * @return True if there is still space and false if there is not
	 */
	public static boolean hasQueueSpaceLeft(GuildMeta g, UserMeta u) {
		//GuildMusicManager m = MusicUtils.getGuildMusicManager(Main.getClient().getGuildById(g.getGuild().getId()).block());
		//return g.isPremium() || u.isPremium() ? m.getScheduler().getQueueSize() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_SONG_QUEUE_PREMIUM)) : m.getScheduler().getQueueSize() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_SONG_QUEUE));
		return true;
	}

	/**
	 * Checks if this guild has any prefixes left that they can set
	 *
	 * @param guild The guild to check for
	 * @param user  The user to check for
	 * @return True if they have prefixes left and false if they don't
	 */
	public static boolean hasPrefixesLeft(Guild guild, User user) {
		GuildMeta g = Caches.getGuild(guild).block();
		UserMeta u = Caches.getUser(user).block();
		return g.isPremium() || u.isPremium() ? g.getPrefixes().size() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_PREFIXES_PREMIUM)) : g.getPrefixes().size() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_PREFIXES));
	}

	/**
	 * Checks if this guild has any prefixes left that they can set
	 *
	 * @param g The guild to check for
	 * @param u The user to check for
	 * @return True if they have prefixes left and false if they don't
	 */
	public static boolean hasPrefixesLeft(GuildMeta g, UserMeta u) {
		return g.isPremium() || u.isPremium() ? g.getPrefixes().size() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_PREFIXES_PREMIUM)) : g.getPrefixes().size() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_PREFIXES));
	}
}
