package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.entity.User;

import java.util.concurrent.TimeUnit;


public class InputDialog extends Dialog<String> {

	public InputDialog(MessageChannel channel, User user, String description) {
		super(DialogType.OPEN,
				(TextChannel) channel,
				user,
				null,
				"input",
				new Object[0],
				description,
				new Object[0],
				null,
				ColorUtil.withinTwoHues(0.166666F, 0.5F),
				1L,
				TimeUnit.MINUTES,
				o -> o);
	}
}
