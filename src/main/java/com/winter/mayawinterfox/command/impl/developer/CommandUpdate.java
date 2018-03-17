package com.winter.mayawinterfox.command.impl.developer;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.UpdateUtil;

import java.util.Collections;

public class CommandUpdate extends Node<Command> {

	public CommandUpdate() {
		super(new Command(
				"update",
				"dev-only",
				PermissionChecks.isGlobal(),
				e -> {
					MessageUtil.sendMessage(e.getChannel(), "updating");
					UpdateUtil.update();
					System.exit(0);
					return true;
				}
		), Collections.emptyList());
	}
}
