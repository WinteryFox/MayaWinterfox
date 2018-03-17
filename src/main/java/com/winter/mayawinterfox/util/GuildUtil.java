package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.music.GuildMusicManager;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class GuildUtil {

	public static IChannel getFirstChannel(IGuild guild) {
		if (guild.getDefaultChannel().getModifiedPermissions(guild.getEveryoneRole()).contains(Permissions.SEND_MESSAGES))
			return guild.getDefaultChannel();

		for (IChannel channel : guild.getChannels())
			if (channel.getModifiedPermissions(guild.getEveryoneRole()).contains(Permissions.SEND_MESSAGES))
				return channel;

		return null;
	}

	public static boolean isPremium(IGuild guild, IUser user) {
		return Caches.getGuild(guild).isPremium() || Caches.getUser(user).isPremium();
	}

	/**
	 * Assign a role to a user
	 * @param target The user to assign the role to
	 * @param role The role to assign
	 */
	public static void addRole(IUser target, IRole role) {
		RequestBuffer.request(() -> target.addRole(role));
	}

	/**
	 * Remove a role from a user
	 * @param target The user to remove the role from
	 * @param role The role to remove
	 */
	public static void removeRole(IUser target, IRole role) {
		RequestBuffer.request(() -> target.removeRole(role));
	}

	/**
	 * Bulk delete messages
	 * @param channel The channel to bulk delete from
	 * @param messages The messages to bulk delete
	 */
	public static void bulkDelete(IChannel channel, List<IMessage> messages) {
		RequestBuffer.request(() -> channel.bulkDelete(messages));
	}

	/**
	 * Checks if the guild has space left in the song queue
	 *
	 * @param guild The guild to check for
	 * @param user  The user to check for
	 * @return True if there is still space and false if there is not
	 */
	public static boolean hasQueueSpaceLeft(IGuild guild, IUser user) {
		GuildMeta g = Caches.getGuild(guild);
		UserMeta u = Caches.getUser(user);
		GuildMusicManager m = MusicUtils.getGuildMusicManager(guild);
		return g.isPremium() || u.isPremium() ? m.getScheduler().getQueueSize() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_SONG_QUEUE_PREMIUM)) : m.getScheduler().getQueueSize() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_SONG_QUEUE));
	}

	/**
	 * Checks if the guild has space left in the song queue
	 *
	 * @param g The guild to check for
	 * @param u The user to check for
	 * @return True if there is still space and false if there is not
	 */
	public static boolean hasQueueSpaceLeft(GuildMeta g, UserMeta u) {
		GuildMusicManager m = MusicUtils.getGuildMusicManager(Main.getClient().getGuildByID(g.getLongID()));
		return g.isPremium() || u.isPremium() ? m.getScheduler().getQueueSize() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_SONG_QUEUE_PREMIUM)) : m.getScheduler().getQueueSize() < Integer.parseUnsignedInt(Main.config.get(Main.ConfigValue.MAX_SONG_QUEUE));
	}

	/**
	 * Checks if this guild has any prefixes left that they can set
	 *
	 * @param guild The guild to check for
	 * @param user  The user to check for
	 * @return True if they have prefixes left and false if they don't
	 */
	public static boolean hasPrefixesLeft(IGuild guild, IUser user) {
		GuildMeta g = Caches.getGuild(guild);
		UserMeta u = Caches.getUser(user);
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
