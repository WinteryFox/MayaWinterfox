package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.TimeUnit;


public class InputDialog extends Dialog<String> {

	public InputDialog(IChannel channel, IUser user, String description) {
		super(DialogType.OPEN,
				channel,
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
