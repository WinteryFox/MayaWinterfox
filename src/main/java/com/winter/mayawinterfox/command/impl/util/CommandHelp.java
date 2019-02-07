package com.winter.mayawinterfox.command.impl.util;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.Collections;
import java.util.HashSet;
import java.util.function.Consumer;

public class CommandHelp extends Node<Command> {

	public CommandHelp() {
		super(new Command(
				"help",
				"help-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					if (args.length == 1) {
						return Commands.sendHelp(e);
					} else if (args.length == 2) {
						Consumer<EmbedCreateSpec> help = Commands.getHelp(e.getGuild().block(), args[1]);
						if (help != null) {
							return MessageUtil.sendMessage(e.getMessage().getChannel().block(), help) != null;
						}
					}
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), "unknown-command");
					return false;
				},
				new HashSet<>(Collections.singletonList("halp"))
		), Collections.emptyList());
	}
}
