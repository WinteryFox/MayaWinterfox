package com.winter.mayawinterfox.command.impl.util;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.HashSet;

public class CommandHelp extends Node<Command> {

	public CommandHelp() {
		super(new Command(
				"help",
				"help-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					if (args.length == 1) {
						return Commands.sendHelp(e);
					} else if (args.length == 2) {
						EmbedObject help = Commands.getHelp(e.getGuild(), args[1]);
						if (help != null) {
							return MessageUtil.sendMessage(e.getChannel(), help) != null;
						}
					}
					MessageUtil.sendMessage(e.getChannel(), "unknown-command");
					return false;
				},
				new HashSet<>(Collections.singletonList("halp"))
		), Collections.emptyList());
	}
}
