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

public class CommandRate extends Node<Command> {

	public CommandRate() {
		super(new Command(
				"rate",
				"rate-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IUser target;
					if (args.length == 2)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("rate ".length()));
					else
						target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;

					MessageUtil.sendMessage(e.getMessage().getChannel().block(), "rate-you", target.getName(), new Random().nextInt(100));
					return true;
				}
		), Collections.emptyList());
	}
}