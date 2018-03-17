package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.http.HTTPHandler;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;

public class CommandUrban extends Node<Command> {

	public CommandUrban() {
		super(new Command(
				"urban",
				"urban-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String q;
					if (args.length > 1) {
						q = MessageUtil.args(e.getMessage()).substring("urban ".length());
					} else
						q = new InputDialog(e.getChannel(), e.getAuthor(), "search-for").open();
					if (q == null)
						return false;
					try {
						MessageUtil.sendMessage(e.getChannel(), HTTPHandler.requestUrban(e.getGuild(), q));
					} catch (Exception ex) {
						ErrorHandler.log(ex, e.getChannel());
					}
					return true;
				}
		), Collections.emptyList());
	}
}