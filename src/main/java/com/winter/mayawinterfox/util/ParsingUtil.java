package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.Main;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Snowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class ParsingUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParsingUtil.class);

	/**
	 * Search for a user
	 * @param s The user string to search for
	 * @return The user that was found
	 * @throws IllegalArgumentException On failure
	 */
	@Nullable
	public static User getUser(String s) {
		LOGGER.debug(String.format("Passed in with `%s`...", s));
		if (s.matches("<@!?\\d+>")) {
			long id = Long.parseLong(s.replaceAll("<@!?(\\d+)>", "$1"));
			return Main.getClient().getUserById(Snowflake.of(id)).block();
		} else if (s.matches("\\d+")) {
			long id = Long.parseLong(s);
			return Main.getClient().getUserById(Snowflake.of(id)).block();
		}
		return null;
	}

	/**
	 * Search for a role
	 * @param guild The guild to search for the role
	 * @param s The role to search for
	 * @return The role that was found
	 */
	@Nullable
	public static Role getRole(Guild guild, String s) {
		LOGGER.debug(String.format("Passed in with `%s`...", s));
		if (s.matches("<@&\\d+>")) {
			long id = Long.parseLong(s.replaceAll("<@&(\\d+)>", "$1"));
			return guild.getRoleById(Snowflake.of(id)).block();
		} else if (s.matches("\\d+")) {
			long id = Long.parseLong(s);
			return guild.getRoleById(Snowflake.of(id)).block();
		} else {
			return guild.getRoles().filter(role -> role.getName().equals(s)).blockFirst();
		}
	}

	@Nullable
	public static TextChannel getChannel(Guild guild, String s) {
		LOGGER.info(String.format("Passed in with '%s'...", s));

		Channel channel;

		if (s.matches("<#\\d+>")) {
			long id = Long.parseLong(s.replaceAll("<#(\\d+)>", "$1"));
			channel = guild.getChannelById(Snowflake.of(id)).block();
		} else if (s.matches("\\d+")) {
			long id = Long.parseLong(s);
			channel = guild.getChannelById(Snowflake.of(id)).block();
		} else {
			channel = guild.getChannels().filter(c -> c.getName().equals(s)).blockFirst();
		}

		if (channel instanceof TextChannel)
			return (TextChannel) channel;
		return null;
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
			builder.append("h:");
		} else if (days > 0) {
			builder.append("00:");
		}

		if (minutes > 0) {
			if (minutes < 10 && (hours > 0 || days > 0))
				builder.append("0");
			builder.append(minutes).append("m");
		} else {
			builder.append("0");
			if (hours > 0 || days > 0)
				builder.append("0");
			builder.append("m");
		}

		builder.append(":");
		if (seconds < 10)
			builder.append("0");
		builder.append(seconds).append("s");

		if (convert < 1f && convert > 0f) {
			return "0:0" + String.format("%.3f", Math.max(convert, 0.001f));
		}

		return builder.toString();
	}
}