package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.json.request.EmbedRequest;
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

	public static Mono<Message> sendMessage(TextChannel channel, String messageKey, Object... params) {
		return channel.createMessage(Localisation.getMessage(channel.getGuild().block(), messageKey, params)).single();
	}

	public static Message sendMessage(TextChannel channel, String messageKey, Consumer<EmbedCreateSpec> embed, InputStream file, String fileName, Object... params) {
		try {
			return new MessageCreateSpec()
					.setContent(Localisation.getMessage(channel.getGuild().block(), messageKey, params))
					.setEmbed(embed)
					.setFile(fileName, file)
					.setTts(false);
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

	public static Message sendMessage(IChannel channel, EmbedObject embed, InputStream file, String fileName) {
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
	public static Message sendMessage(IChannel channel, EmbedObject embed) {
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
