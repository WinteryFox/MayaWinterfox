package com.winter.mayawinterfox.command.impl.image;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import discord4j.core.object.util.Permission;

import java.util.Collections;
import java.util.HashSet;

public class CommandCat extends Node<Command> {

	public CommandCat() {
		super(new Command(
				"cat",
				"cat-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					//try {
					//MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.imageEmbed(e.getGuild().block(), HTTPHandler.requestCat()));
					//	} catch (MalformedURLException | UnirestException ex) {
					//		ErrorHandler.log(ex, e.getMessage().getChannel().block());
					//	}
					return true;
				},
				new HashSet<>(Collections.singletonList("kitty"))
		), Collections.emptyList());
	}
}