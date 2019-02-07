package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;

import java.util.Collections;
import java.util.Random;

public class CommandKawaii extends Node<Command> {

	public CommandKawaii() {
		super(new Command(
				"kawaii",
				"kawaii-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					Member target;
					if (args.length == 2)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("kawaii ".length())).asMember(e.getGuildId().get()).block();
					else
						target = new TargetDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;

					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "is-kawaii", target.getUsername(), new Random().nextInt(100)));
					return true;
				}
		), Collections.emptyList());
	}
}
