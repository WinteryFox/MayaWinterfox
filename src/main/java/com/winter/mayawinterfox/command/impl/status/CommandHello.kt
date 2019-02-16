package com.winter.mayawinterfox.command.impl.status

import com.winter.mayawinterfox.command.Command
import com.winter.mayawinterfox.command.CommandPermission
import com.winter.mayawinterfox.util.MessageUtil
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class CommandHello : Command(
		"hello",
		"hello-help",
		Command.Category.STATUS,
		CommandPermission(PermissionSet.of(Permission.SEND_MESSAGES), PermissionSet.of(Permission.SEND_MESSAGES)),
		setOf("hi")
) {
	override fun call(event: MessageCreateEvent): Mono<Void> {
		return event.message.channel
				.flatMap { c -> MessageUtil.sendMessage(c, "hello") }
				.then()
	}
}