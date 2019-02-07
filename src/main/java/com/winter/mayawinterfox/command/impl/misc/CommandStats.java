package com.winter.mayawinterfox.command.impl.misc;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import discord4j.core.object.util.Permission;

import java.util.Collections;

public class CommandStats extends Node<Command> {

	public CommandStats() {
		super(new Command(
				"stats",
				"stats-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {

					return true;
				}
		), Collections.emptyList());
	}
}
