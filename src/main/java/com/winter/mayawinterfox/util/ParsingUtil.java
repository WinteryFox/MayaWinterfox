package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.Main;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class ParsingUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParsingUtil.class);

	/**
	 * Search for a user
	 * @param s The user string to search for
	 * @return The user that was found
	 * @throws IllegalArgumentException On failure
	 */
	public static User getUser(String s) {
		LOGGER.debug(String.format("Passed in with `%s`...", s));
		if (s.matches("<@!?\\d+>")) {
			long id = Long.parseLong(s.replaceAll("<@!?(\\d+)>", "$1"));
			return Main.getClient().getUserById(Snowflake.of(id)).block();
		} else if (s.matches("\\d+")) {
			long id = Long.parseLong(s);
			return Main.getClient().getUserById(Snowflake.of(id)).block();
		} else if (s.matches("[^#]+#\\d{4}")) {
			//return Main.getClient().getUsersByName(s.replaceAll("([^#]+)#\\d{4}", "$1")).parallelStream().filter(u -> u.getDiscriminator().equals(s.replaceAll(".+#(\\d{4})", "$1"))).findFirst().orElseThrow(() -> new IllegalArgumentException("No users found"));
			return Main.getClient().getUsers().filter(user -> {
				//return user.getUsername().equals(s.replaceAll("([^#]+)#\\d{4}", "$1")) && user.getDiscriminator().equals(s.replace(".+#(\\d{4})", "$1"));
			}).collectList().block().get(0);
		}
		throw new IllegalArgumentException("Not a valid user string!");
	}

	/**
	 * Search for a role
	 * @param guild The guild to search for the role
	 * @param s The role to search for
	 * @return The role that was found
	 * @throws IllegalArgumentException On failure
	 */
	public static IRole getRole(IGuild guild, String s) {
		LOGGER.debug(String.format("Passed in with `%s`...", s));
		if (s.matches("<@&\\d+>")) {
			long id = Long.parseLong(s.replaceAll("<@&(\\d+)>", "$1"));
			return guild.getRoleByID(id);
		} else if (s.matches("\\d+")) {
			long id = Long.parseLong(s);
			return guild.getRoleByID(id);
		} else {
			return guild.getRolesByName(s).parallelStream().findFirst().orElseThrow(() -> new IllegalArgumentException("No roles found."));
		}
	}
	
	public static IChannel getChannel(IGuild guild, String s) {
		LOGGER.info(String.format("Passed in with '%s'...", s));
		if (s.matches("<#\\d+>")) {
			long id = Long.parseLong(s.replaceAll("<#(\\d+)>", "$1"));
			return guild.getChannelByID(id);
		} else if (s.matches("\\d+")) {
			long id = Long.parseLong(s);
			return guild.getChannelByID(id);
		} else {
			return guild.getChannelsByName(s).parallelStream().findFirst().orElseThrow(() -> new IllegalArgumentException("No channels found"));
		}
	}

	/**
	 * Format a time in milliseconds to dd:hh:mm:ss
	 * @param milliseconds The milliseconds to format
	 * @return The formatted time
	 */
	public static String formatTime(long milliseconds) {
		StringBuilder builder = new StringBuilder();
		long convert = (milliseconds / 1000);
		int days = (int) (convert / (3600 * 24));
		int hours = (int) (convert / 3600 % 24);
		int minutes = (int) (convert / 60 % 60);
		int seconds = (int) (convert % 60);

		if (days > 0) {
			builder.append(days);
			builder.append(":");
		}

		if (hours > 0) {
			if (hours < 10)
				builder.append("0");
			builder.append(hours);
			builder.append(":");
		} else if (days > 0) {
			builder.append("00:");
		}

		if (minutes > 0) {
			if (minutes < 10 && (hours > 0 || days > 0))
				builder.append("0");
			builder.append(minutes);
		} else {
			builder.append("0");
			if (hours > 0 || days > 0)
				builder.append("0");
		}

		builder.append(":");
		if (seconds < 10)
			builder.append("0");
		builder.append(seconds);

		if (convert < 1f && convert > 0f) {
			return "0:0" + String.format("%.3f", Math.max(convert, 0.001f));
		}

		return builder.toString();
	}
}