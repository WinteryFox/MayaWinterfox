package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.TextChannel;

import java.util.concurrent.TimeUnit;

public class RoleDialog extends Dialog<Role> {

	public RoleDialog(TextChannel channel, Member user) {
		super(DialogType.OPEN,
				channel,
				user,
				null,
				"role-dialog",
				new Object[0],
				"role-dialog-desc",
				new Object[0],
				null,
				ColorUtil.withinTwoHues(0.333333333F, 0.8333333333F),
				1L,
				TimeUnit.MINUTES,
				o -> {
					if (o == null)
						return null;
					return ParsingUtil.getRole(channel.getGuild().block(), o);
				});
	}
}