package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.util.Permission;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class CommandCoinFlip extends Node<Command> {

	public CommandCoinFlip() {
		super(new Command(
				"coinflip",
				"coinflip-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				o -> {
					int result = new Random().nextInt(2);
					if (result == 1)
						MessageUtil.sendMessage(o.getMessage().getChannel().block(), EmbedUtil.successEmbed(o.getGuild().block(), "coinflip-heads"));
					else
						MessageUtil.sendMessage(o.getMessage().getChannel().block(), EmbedUtil.successEmbed(o.getGuild().block(), "coinflip-tails"));
					return true;
				},
				new HashSet<>(Collections.singletonList("cf"))
		), Collections.emptyList());
	}
}
