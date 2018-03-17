package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.AdminUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class CommandBan extends Node<Command> {

	public CommandBan() {
		super(new Command(
				"ban",
				"ban-help",
				PermissionChecks.hasPermission(Permissions.BAN),
				AdminUtil::ban,
				new HashSet<>(Arrays.asList("bean", "hammer"))
		), Collections.emptyList());
	}
}
