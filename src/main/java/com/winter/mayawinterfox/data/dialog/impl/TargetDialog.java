package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.TimeUnit;

public class TargetDialog extends Dialog<IUser> {

	public TargetDialog(IChannel channel, IUser user) {
		super(DialogType.OPEN,
				channel,
				user,
				null,
				"enter-target",
				new Object[0],
				"enter-target-desc",
				new Object[0],
				null,
				ColorUtil.withinTwoHues(0.5F, 0.833333F),
				1L,
				TimeUnit.MINUTES,
				o -> o == null ? null : ParsingUtil.getUser(o));
	}
}
