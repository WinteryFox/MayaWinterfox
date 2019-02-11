package com.winter.mayawinterfox.command.impl.admin

import com.winter.mayawinterfox.checks.PermissionChecks
import com.winter.mayawinterfox.command.Command
import com.winter.mayawinterfox.data.Node
import discord4j.core.`object`.util.Permission
import discord4j.core.event.domain.message.MessageCreateEvent
import java.util.*
import java.util.function.Predicate

class CommandRSS : Node<Command>(Command(
		"",
		"",
		PermissionChecks.hasPermission(Permission.MANAGE_CHANNELS),
		Predicate{e: MessageCreateEvent ->

        },
		Collections.emptySet()
		), Collections.emptyList())