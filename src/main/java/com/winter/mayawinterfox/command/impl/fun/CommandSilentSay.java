package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.util.Permission;

import java.util.Collections;

public class CommandSilentSay extends Node<Command> {

	public CommandSilentSay() {
		super(new Command(
				"ssay",
				"say-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String i = null;
					if (args.length <= 1)
						i = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-say").open();
					if (args.length > 1)
						i = MessageUtil.args(e.getMessage()).substring("ssay ".length());

					MessageUtil.sendRawMessage(e.getMessage().getChannel().block(), i);
					e.getMessage().delete().block();
					return true;
				}
		), Collections.emptyList());
	}
}
