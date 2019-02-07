package com.winter.mayawinterfox.exceptions;

import discord4j.core.object.entity.MessageChannel;

public class ErrorHandler {

	/**
	 * DO NOT USE THIS METHOD IF THE ERROR OCCURRED WHILE HANDLING A COMMAND!!!!!
	 * @param t The throwable that caused the error
	 * @param meta The meta message to append
	 */
	public static void log(Throwable t, String meta) {
		/*MessageUtil.sendMessage(Main.getClient().getChannelByID(Long.parseLong(Main.config.get(Main.ConfigValue.ERROR_CHANNEL))),
				spec -> spec
						.setColor(ColorUtil.withinTwoHues(0.7f, 1.2f))
						.setTitle("An error has occurred! " + meta)
						.setDescription("```\n")
						.appendDescription(t.toString())
						.appendDescription(Arrays.toString(t.getStackTrace()))
						.appendDescription("\n```")
						.build());*/
	}

	/**
	 * Log an error to the error channel and also notify the user
	 * @param t The throwable that caused the error
	 * @param channel The channel to notify the user in
	 */
	public static void log(Throwable t, MessageChannel channel) {
		/*if (!(t instanceof UpdateFailedException) && !(t instanceof NumberFormatException)) {
			MessageUtil.sendMessage(Main.getClient().getChannelByID(Long.parseLong(Main.config.get(Main.ConfigValue.ERROR_CHANNEL))),
					new EmbedBuilder()
							.setLenient(false)
							.withColor(ColorUtil.withinTwoHues(0.7f, 1.2f))
							.withTitle("An error has occurred!")
							.appendDescription("```\n")
							.appendDescription(t.toString())
							.appendDescription(Arrays.toString(t.getStackTrace()))
							.appendDescription("\n```")
							.build());
			t.printStackTrace();
		}
		MessageUtil.sendMessage(channel,
				new EmbedBuilder()
						.setLenient(false)
						.withColor(ColorUtil.withinTwoHues(0.7f, 1.2f))
						.withTitle(Localisation.getMessage(channel.getGuild(), "error"))
						.withDescription(t.getMessage())
						.build());*/
	}

	/**
	 * Log an error to the error channel and also notify the user
	 *
	 * @param t          The throwable that caused the error
	 * @param channel    The channel to notify the user in
	 * @param messageKey The error message to put into the channel
	 */
	public static void log(Throwable t, MessageChannel channel, String messageKey) {
		/*if (!(t instanceof UpdateFailedException) && !(t instanceof NumberFormatException)) {
			MessageUtil.sendMessage(Main.getClient().getChannelByID(Long.parseLong(Main.config.get(Main.ConfigValue.ERROR_CHANNEL))),
							builder -> builder
							.withColor(ColorUtil.withinTwoHues(0.7f, 1.2f))
							.withTitle("An error has occurred!")
							.appendDescription("```\n")
							.appendDescription(t.toString())
							.appendDescription(Arrays.toString(t.getStackTrace()))
							.appendDescription("\n```")
							.build());
			t.printStackTrace();
		}
		MessageUtil.sendMessage(channel,
				new EmbedBuilder()
						.setLenient(false)
						.withColor(ColorUtil.withinTwoHues(0.7f, 1.2f))
						.withTitle(Localisation.getMessage(channel.getGuild(), "error"))
						.withDescription(Localisation.getMessage(channel.getGuild(), messageKey))
						.build());*/
	}
}
