package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.TextChannel;

import java.util.concurrent.TimeUnit;

public class ChannelDialog extends Dialog<TextChannel> {

	public ChannelDialog(TextChannel channel,
	                     Member user) {
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
				o -> o == null ? null : ParsingUtil.getChannel(channel.getGuild().block(), o));
	}
}
