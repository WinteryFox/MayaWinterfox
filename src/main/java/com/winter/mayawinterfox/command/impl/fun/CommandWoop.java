package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.util.Permission;

import java.util.Collections;

public class CommandWoop extends Node<Command> {

	public CommandWoop() {
		super(new Command(
				"woop",
				"woop-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.imageEmbed(e.getGuild().block(), "https://i.imgur.com/oVvHXXS.gif"));
					return true;
				}
		), Collections.emptyList());
	}
}
