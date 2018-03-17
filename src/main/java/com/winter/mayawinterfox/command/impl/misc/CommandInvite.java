package com.winter.mayawinterfox.command.impl.misc;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;

public class CommandInvite extends Node<Command> {

	public CommandInvite() {
		super(new Command(
				"invite",
				"invite-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					MessageUtil.sendMessage(e.getChannel(), new EmbedBuilder()
							.withColor(ColorUtil.withinTwoHues(0.3333333f, 0.8888888f))
							.withThumbnail(Main.getClient().getApplicationIconURL())
							.withTimestamp(Instant.now())
							.withTitle("Invite")
							.withDesc("https://discordapp.com/oauth2/authorize?client_id=289381714885869568&scope=bot&permissions=372435975")
							.build());
					return true;
				}
		), Collections.emptyList());
	}
}
