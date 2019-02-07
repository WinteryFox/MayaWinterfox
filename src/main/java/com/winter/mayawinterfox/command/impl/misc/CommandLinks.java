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
import java.time.LocalDateTime;
import java.util.Collections;

public class CommandLinks extends Node<Command> {

	public CommandLinks() {
		super(new Command(
			"links",
				"links-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), spec -> spec
							.setColor(ColorUtil.withinTwoHues(0.3333333f, 0.88888888f))
							.setThumbnail(Main.getClient().getApplicationInfo().block().getIcon(Image.Format.PNG).get())
							.setTimestamp(Instant.now())
							.addField("Invite", "https://discordapp.com/oauth2/authorize?client_id=289381714885869568&scope=bot&permissions=372435975", false)
							.addField("Patreon", "https://www.patreon.com/HoroBot", false)
							.addField("Discord", "http://www.discord.gg/MCUTSZz", false));
					return true;
				}
		), Collections.emptyList());
	}
}
