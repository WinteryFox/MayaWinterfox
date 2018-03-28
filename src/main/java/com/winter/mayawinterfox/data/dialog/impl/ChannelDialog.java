package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.TimeUnit;

public class ChannelDialog extends Dialog<String> {

	public ChannelDialog(IChannel channel,
	                     IUser user) {
		super(DialogType.OPEN,
		      channel,
		      user,
		      null,
		      "input-channel-title",
		      new Object[0],
		      "input-channel-description",
		      new Object[0],
		      null,
		      ColorUtil.withinTwoHues(0.3333f, 0.8888f),
		      1L,
		      TimeUnit.MINUTES,
		      o -> o == null ? null : ParsingUtil.getChannel(o));
	}
}
