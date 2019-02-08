package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class MessageUtil {

	public static String[] argsArray(Message m) {
		Optional<String> o = Caches.getGuild(Objects.requireNonNull(m.getGuild().block())).getPrefixes().stream().filter(m.getContent().get()::startsWith).findAny();
		return o.map(s -> m.getContent().get().substring(s.length())).orElseGet(m.getContent()::toString).split("\\s+");
	}

	public static String[] argsArray(Guild g, String m) {
		Optional<String> o = Caches.getGuild(g).getPrefixes().stream().filter(m::startsWith).findAny();
		return o.map(s -> m.substring(s.length())).orElse(m).split("\\s+");
	}

	public static String args(Message m) {
		return String.join(" ", argsArray(m));
	}

	public static Message sendMessage(MessageChannel channel, String messageKey, Object... params) {
		return channel.createMessage(Localisation.getMessage(((GuildChannel) channel).getGuild().block(), messageKey, params)).block();
	}

	public static Message sendMessage(MessageChannel channel, String messageKey, Consumer<EmbedCreateSpec> embed, InputStream file, String fileName, Object... params) {
		try {
			return channel.createMessage(spec -> spec
					.setContent(Localisation.getMessage(((GuildChannel) channel).getGuild().block(), messageKey, params))
					.setEmbed(embed)
					.setFile(fileName, file)
					.setTts(false)).block();
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

	public static Message sendMessage(MessageChannel channel, Consumer<EmbedCreateSpec> embed, InputStream file, String fileName) {
		try {
			return channel.createMessage(spec -> spec
					.setFile(fileName, file)
					.setTts(false)).block();
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
	public static Message sendMessage(MessageChannel channel, Consumer<EmbedCreateSpec> embed) {
		return channel.createMessage(spec -> spec.setEmbed(embed)).block();
	}

	/**
	 * Send an embed in a channel with a message
	 * @param channel The channel to send the message in
	 * @param embed The embed object to send
	 * @param messageKey The localisation key for the message
	 * @param params The replacements for %s in the message
	 */
	public static Message sendMessage(MessageChannel channel, Consumer<EmbedCreateSpec> embed, String messageKey, Object... params) {
		return channel.createMessage(spec ->
				spec.setContent(Localisation.getMessage(((GuildChannel) channel).getGuild().block(), messageKey, params))
				.setEmbed(embed)).block();
	}

	/**
	 * Send a raw message in a channel
	 * @param channel The channel to send the message in
	 * @param message The message to send
	 */
	public static Message sendRawMessage(MessageChannel channel, String message) {
		return channel.createMessage(spec -> spec.setContent(message)).block();
	}

	/**
	 * Adds a reaction to a message
	 * @param message The message to add the reaction to
	 * @param emoji The emoji to react with
	 */
	public static void addReaction(Message message, ReactionEmoji emoji) {
		message.addReaction(emoji).block();
	}

	/**
	 * Delete a message
	 * @param message The message to delete
	 */
	public static void delete(Message message) {
		message.delete().block();
	}
}
