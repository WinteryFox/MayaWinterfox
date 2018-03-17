package com.winter.mayawinterfox.command.impl.misc;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;

public class CommandStats extends Node<Command> {

	public CommandStats() {
		super(new Command(
				"stats",
				"stats-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {

					return true;
				}
		), Collections.emptyList());
	}
}
