package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.TextChannel;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CommandDialog extends Dialog<Node<Command>> {

	public CommandDialog(TextChannel channel, Member user, Map<String, Node<Command>> choices) {
		super(DialogType.CHOICE,
				channel,
				user,
				null,
				"select-subnode",
				new Object[0],
				"select-subnode-desc",
				new Object[0],
				choices,
				ColorUtil.withinTwoHues(0.333333333F, 0.8333333333F),
				1L,
				TimeUnit.MINUTES,
				null);
	}
}
