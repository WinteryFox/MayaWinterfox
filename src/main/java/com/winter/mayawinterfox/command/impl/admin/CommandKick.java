package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.AdminUtil;
import discord4j.core.object.util.Permission;

import java.util.Collections;
import java.util.HashSet;

public class CommandKick extends Node<Command> {

	public CommandKick() {
		super(new Command(
				"kick",
				"kick-help",
				PermissionChecks.hasPermission(Permission.KICK_MEMBERS),
				AdminUtil::kick,
				new HashSet<>(Collections.singletonList("eject"))
		), Collections.emptyList());
	}
}
