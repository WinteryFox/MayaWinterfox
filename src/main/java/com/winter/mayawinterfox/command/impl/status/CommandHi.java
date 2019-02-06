package com.winter.mayawinterfox.command.impl.status;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;

public class CommandHi extends Node<Command> {

	public CommandHi() {
		super(new Command(
				"hi",
				"hi-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), "hello");
					return true;
				}
		), Collections.emptyList());
	}
}