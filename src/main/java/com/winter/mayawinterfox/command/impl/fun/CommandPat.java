package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.Random;

public class CommandPat extends Node<Command> {

	public CommandPat() {
		super(new Command(
				"pat",
				"pat-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IUser target;
					if (args.length == 2)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("pat ".length()));
					else
						target = (IUser) new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
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