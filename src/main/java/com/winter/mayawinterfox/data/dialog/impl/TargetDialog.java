package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.TextChannel;

import java.util.concurrent.TimeUnit;

public class TargetDialog extends Dialog<Member> {

	public TargetDialog(MessageChannel channel, Member user) {
		super(DialogType.OPEN,
				((TextChannel) channel),
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
				o -> o == null ? null : ParsingUtil.getUser(o).asMember(((TextChannel) channel).getGuildId()).block());
	}
}
