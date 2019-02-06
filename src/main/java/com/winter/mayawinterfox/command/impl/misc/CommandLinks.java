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

public class CommandLinks extends Node<Command> {

	public CommandLinks() {
		super(new Command(
			"links",
				"links-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), new EmbedBuilder()
							.withColor(ColorUtil.withinTwoHues(0.3333333f, 0.88888888f))
							.withThumbnail(Main.getClient().getApplicationIconURL())
							.withTimestamp(Instant.now())
							.appendField("Invite", "https://discordapp.com/oauth2/authorize?client_id=289381714885869568&scope=bot&permissions=372435975", false)
							.appendField("Patreon", "https://www.patreon.com/HoroBot", false)
							.appendField("Discord", "http://www.discord.gg/MCUTSZz", false)
							.build());
					return true;
				}
		), Collections.emptyList());
	}
}
