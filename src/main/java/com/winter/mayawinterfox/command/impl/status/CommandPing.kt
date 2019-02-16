package com.winter.mayawinterfox.command.impl.status

import com.winter.mayawinterfox.command.Command
import com.winter.mayawinterfox.command.PermissionManager
import com.winter.mayawinterfox.data.Node
import discord4j.core.`object`.entity.TextChannel
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.PermissionSet
import java.util.function.Consumer

class CommandPing : Node<Command>(
		Command(
				"ping",
				"ping-help",
				Command.Category.STATUS,
				PermissionManager(PermissionSet.of(Permission.SEND_MESSAGES), PermissionSet.of(Permission.SEND_MESSAGES)),
				Consumer { e ->
					e.message.channel.ofType(TextChannel::class.java)
							.map { c ->
								c.createMessage("ping!")
							}
				}
		),
		emptyList()
)