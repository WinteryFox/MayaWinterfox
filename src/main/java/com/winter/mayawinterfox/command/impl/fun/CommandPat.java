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

public class CommandPat extends Node<Command> {

	public CommandPat() {
		super(new Command(
				"pat",
				"pat-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					Member target;
					if (args.length == 2)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("pat ".length())).asMember(e.getGuildId().get()).block();
					else
						target = (Member) new TargetDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;
					String[] images = {
							""
					};

					if (e.getMember().get().equals(target))
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.imageEmbed(e.getGuild().block(), images[new Random().nextInt(images.length)]), "pat-from", target, e.getMember().get());
					else
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.imageEmbed(e.getGuild().block(), images[new Random().nextInt(images.length)]), "pat-from-yourself", target);
					return true;
				}
		), Collections.emptyList());
	}
}