package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.Random;

public class CommandKiss extends Node<Command> {

	public CommandKiss() {
		super(new Command(
				"kiss",
				"kiss-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IUser target;
					if (args.length == 2)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("kiss ".length()));
					else
						target = (IUser) new TargetDialog(e.getChannel(), e.getAuthor()).open();
					if (target == null)
						return false;
					String[] images = {
							""
					};

					EmbedObject embed = EmbedUtil.imageEmbed(e.getGuild(), images[new Random().nextInt(images.length)]);

					if (e.getAuthor().equals(target))
						MessageUtil.sendMessage(e.getChannel(), embed, "kiss-from-yourself", e.getAuthor().getName());
					else
						MessageUtil.sendMessage(e.getChannel(), embed, "kiss-from", target, e.getAuthor().getName());
					return true;
				}
		), Collections.emptyList());
	}
}
