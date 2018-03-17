package com.winter.mayawinterfox.command.impl.animal;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;

public class CommandShop extends Node<Command> {

	public CommandShop() {
		super(new Command(
				"shop",
				"shop-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					MessageUtil.sendMessage(e.getChannel(), "visit-shop");
					return true;
				}
		), Collections.emptyList());
	}
}
