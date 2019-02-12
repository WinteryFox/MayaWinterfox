package com.winter.mayawinterfox.command.impl.admin

import com.winter.mayawinterfox.checks.PermissionChecks
import com.winter.mayawinterfox.command.Command
import com.winter.mayawinterfox.data.Node
import com.winter.mayawinterfox.data.cache.Caches
import com.winter.mayawinterfox.data.http.Feed
import com.winter.mayawinterfox.util.MessageUtil
import discord4j.core.`object`.util.Permission
import reactor.core.publisher.Mono
import reactor.core.publisher.onErrorReturn
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

class CommandRSS : Node<Command>(Command(
		"rss",
		"rss-help",
		PermissionChecks.hasPermission(Permission.MANAGE_CHANNELS),
        Predicate { e ->
            val guild = e.guild.flatMap { g -> Caches.getGuild(g) }
			val channel = e.message.channel
			val message = guild.map { g -> g.feeds }
					.map { f -> f.map { s -> s.feed }.joinToString() }

			Mono.zip(channel, message, MessageUtil::sendRawMessage)
					.block()

			true
        },
		Collections.emptySet()
		), Collections.emptyList())