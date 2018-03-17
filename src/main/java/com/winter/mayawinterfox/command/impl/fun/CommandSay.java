package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;

public class CommandSay extends Node<Command> {

	public CommandSay() {
		super(new Command(
				"say",
				"say-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String i = null;
					if (args.length <= 1)
						i = new InputDialog(e.getChannel(), e.getAuthor(), "input-say").open();
					if (args.length > 1)
						i = MessageUtil.args(e.getMessage()).substring("say ".length());

					MessageUtil.sendRawMessage(e.getChannel(), i);
					return true;
				}
		), Collections.emptyList());
	}
}
