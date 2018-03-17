package com.winter.mayawinterfox.util;

import com.vdurmont.emoji.Emoji;
import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageUtil {

	public static String[] argsArray(IMessage m) {
		Optional<String> o = Caches.getGuild(m.getGuild()).getPrefixes().stream().filter(m.getContent()::startsWith).findAny();
		return o.map(s -> m.getContent().substring(s.length())).orElseGet(m::getContent).split("\\s+");
	}

	public static String[] argsArray(IGuild g, String m) {
		Optional<String> o = Caches.getGuild(g).getPrefixes().stream().filter(m::startsWith).findAny();
		return o.map(s -> m.substring(s.length())).orElse(m).split("\\s+");
	}

	public static String args(IMessage m) {
		return Arrays.stream(argsArray(m)).collect(Collectors.joining(" "));
	}

	public static IMessage sendMessage(IChannel channel, String messageKey, Object... params) {
		return RequestBuffer.request(() -> { return channel.sendMessage(Localisation.getMessage(channel.getGuild(), messageKey, params)); }).get();
	}

	public static IMessage sendMessage(IChannel channel, String messageKey, EmbedObject embed, InputStream file, String fileName, Object... params) {
		try {
			return new MessageBuilder(Main.getClient())
					.withChannel(channel)
					.withContent(Localisation.getMessage(channel.getGuild(), messageKey, params))
					.withEmbed(embed)
					.withFile(file, fileName)
					.send();
		} catch (Exception e) {
			ErrorHandler.log(e, channel, "error");
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException ex) {
					ErrorHandler.log(ex, "error");
				}
			}
		}
		return null;
	}

	public static IMessage sendMessage(IChannel channel, EmbedObject embed, InputStream file, String fileName) {
		try {
			return RequestBuffer.request(() -> {
				return channel.sendFile(embed, file, fileName);
			}).get();
		} catch (Exception e) {
			ErrorHandler.log(e, channel, "error");
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException ex) {
					ErrorHandler.log(ex, "error");
				}
			}
		}
		return null;
	}

	/**
	 * Send an embed in a channel
	 * @param channel The channel to send the message in
	 * @param embed The embed object to send
	 */
	public static IMessage sendMessage(IChannel channel, EmbedObject embed) {
		return RequestBuffer.request(() -> {
			return channel.sendMessage(embed);
		}).get();
	}

	/**
	 * Send an embed in a channel with a message
	 * @param channel The channel to send the message in
	 * @param embed The embed object to send
	 * @param messageKey The localisation key for the message
	 * @param params The replacements for %s in the message
	 */
	public static IMessage sendMessage(IChannel channel, EmbedObject embed, String messageKey, Object... params) {
		return RequestBuffer.request(() -> { return channel.sendMessage(Localisation.getMessage(channel.getGuild(), messageKey, params), embed); }).get();
	}

	/**
	 * Send a raw message in a channel
	 * @param channel The channel to send the message in
	 * @param message The message to send
	 */
	public static IMessage sendRawMessage(IChannel channel, String message) {
		return RequestBuffer.request(() -> { return channel.sendMessage(message); }).get();
	}

	/**
	 * Adds a reaction to a message
	 * @param message The message to add the reaction to
	 * @param emoji The emoji to react with
	 */
	public static void addReaction(IMessage message, Emoji emoji) {
		RequestBuffer.request(() -> message.addReaction(emoji)).get();
	}

	/**
	 * Adds a reaction to a message
	 * @param message The message to add the reaction to
	 * @param emoji The emoji to react with
	 */
	public static void addReaction(IMessage message, ReactionEmoji emoji) {
		RequestBuffer.request(() -> message.addReaction(emoji)).get();
	}

	/**
	 * Delete a message
	 * @param message The message to delete
	 */
	public static void delete(IMessage message) {
		RequestBuffer.request(message::delete).get();
	}
}
