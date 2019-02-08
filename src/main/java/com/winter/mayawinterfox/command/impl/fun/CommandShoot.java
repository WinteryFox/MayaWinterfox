package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;

import java.util.Collections;
import java.util.Random;

public class CommandShoot extends Node<Command> {

	public CommandShoot() {
		super(new Command(
			"shoot",
				"shoot-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					Member target;
					if (args.length > 1)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("shoot ".length())).asMember(e.getGuildId().get()).block();
					else
						target = new TargetDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;

					if (e.getMember().get().equals(target))
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), "shoot-at-yourself", e.getMember().get().getDisplayName());
					else
						if (new Random().nextInt(2) == 1)
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), "shoot-at", e.getMember().get().getDisplayName(), target.getDisplayName(), new Random().nextInt(1000));
						else
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), "shoot-at-miss", e.getMember().get().getDisplayName(), target.getDisplayName());
					return true;
				}
		), Collections.emptyList());
	}
}
