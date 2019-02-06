package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.Random;

public class CommandEightball extends Node<Command> {

	public CommandEightball() {
		super(new Command(
				"8ball",
				"8ball-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String question;
					if (args.length > 1) {
						question = MessageUtil.args(e.getMessage()).substring("urban ".length());
					} else
						question = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-question").open();
					if (question == null)
						return false;

					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "8ball-" + new Random().nextInt(20)));
					return true;
				}
		), Collections.emptyList());
	}
}
