package com.winter.mayawinterfox.command.impl.status;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.StatusUtil;
import sx.blah.discord.handle.obj.Permission;

import java.util.Collections;
import java.util.HashSet;

public class CommandPing extends Node<Command> {

	public CommandPing() {
		super(new Command(
				"ping",
				"ping-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				StatusUtil::ping,
				new HashSet<>(Collections.singleton("pong"))
		), Collections.emptyList());
	}
}
