package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.TimeUnit;

public class RoleDialog extends Dialog<IRole> {

	public RoleDialog(IChannel channel, IUser user) {
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
					return ParsingUtil.getRole(channel.getGuild(), o);
				});
	}
}