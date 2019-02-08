package com.winter.mayawinterfox.exceptions;

import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.TextChannel;

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
		MessageUtil.sendMessage(channel,
				spec -> spec
						.setColor(ColorUtil.withinTwoHues(0.7f, 1.2f))
						.setTitle(Localisation.getMessage(((TextChannel) channel).getGuild().block(), "error"))
						.setDescription(t.getMessage()));
	}

	/**
	 * Log an error to the error channel and also notify the user
	 *
	 * @param t          The throwable that caused the error
	 * @param channel    The channel to notify the user in
	 * @param messageKey The error message to put into the channel
	 */
	public static void log(Throwable t, MessageChannel channel, String messageKey) {
		MessageUtil.sendMessage(channel,
				spec -> spec
						.setColor(ColorUtil.withinTwoHues(0.7f, 1.2f))
						.setTitle(Localisation.getMessage(((TextChannel) channel).getGuild().block(), "error"))
						.setDescription(Localisation.getMessage(((TextChannel) channel).getGuild().block(), messageKey)));
	}
}
