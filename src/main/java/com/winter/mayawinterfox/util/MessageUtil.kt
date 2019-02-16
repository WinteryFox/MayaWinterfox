package com.winter.mayawinterfox.util

import com.winter.mayawinterfox.data.locale.Localisation
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.TextChannel
import discord4j.core.spec.EmbedCreateSpec
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.function.Consumer

object MessageUtil {
	@JvmStatic
	fun sendMessage(channel: MessageChannel, messageKey: String, vararg params: Any): Mono<Message> {
		return channel.toMono()
				.ofType(TextChannel::class.java)
				.flatMap { c -> c.guild.map { g -> Localisation.getMessage(g, messageKey, *params) } }
				.flatMap { m -> channel.createMessage(m) }
	}

	@JvmStatic
	fun sendMessage(channel: MessageChannel, embed: Consumer<EmbedCreateSpec>): Mono<Message> {
		return channel.createMessage { it.setEmbed(embed) }
	}

	@JvmStatic
	fun sendRawMessage(channel: MessageChannel, content: String): Mono<Message> {
		return channel.createMessage(content)
	}
}