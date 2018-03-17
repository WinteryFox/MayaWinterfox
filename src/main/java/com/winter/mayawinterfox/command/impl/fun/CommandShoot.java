package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.Random;

public class CommandShoot extends Node<Command> {

	public CommandShoot() {
		super(new Command(
			"shoot",
				"shoot-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IUser target;
					if (args.length > 1)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("shoot ".length()));
					else
						target = new TargetDialog(e.getChannel(), e.getAuthor()).open();
					if (target == null)
						return false;

					if (e.getAuthor().equals(target))
						MessageUtil.sendMessage(e.getChannel(), "shoot-at-yourself", e.getAuthor().getName());
					else
						if (new Random().nextInt(2) == 1)
							MessageUtil.sendMessage(e.getChannel(), "shoot-at", e.getAuthor().getName(), target.getName(), new Random().nextInt(1000));
						else
							MessageUtil.sendMessage(e.getChannel(), "shoot-at-miss", e.getAuthor().getName(), target.getName());
					return true;
				}
		), Collections.emptyList());
	}
}
