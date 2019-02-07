package com.winter.mayawinterfox.command.impl.misc;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.util.Image;
import discord4j.core.object.util.Permission;

import java.time.Instant;
import java.util.Collections;

public class CommandInvite extends Node<Command> {

	public CommandInvite() {
		super(new Command(
				"invite",
				"invite-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), spec -> spec
							.setColor(ColorUtil.withinTwoHues(0.3333333f, 0.8888888f))
							.setThumbnail(Main.getClient().getApplicationInfo().block().getIcon(Image.Format.PNG).get())
							.setTimestamp(Instant.now())
							.setTitle("Invite")
							.setDescription("https://discordapp.com/oauth2/authorize?client_id=289381714885869568&scope=bot&permissions=372435975"));
					return true;
				}
		), Collections.emptyList());
	}
}
