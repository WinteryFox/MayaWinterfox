package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ColorUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.TimeUnit;

public class AmountDialog extends Dialog<Integer> {

	public AmountDialog(IChannel channel, IUser user, int min, int max) {
		super(DialogType.OPEN,
				channel,
				user,
				null,
				"amount",
				new Object[0],
				"amount-desc",
				new Object[] { min, max },
				null,
				ColorUtil.withinTwoHues(0.333333333F, 0.8333333333F),
				1L,
				TimeUnit.MINUTES,
				o -> {
					if (o == null)
						return 0;
					int amount = Integer.parseUnsignedInt(o);
					if (amount < min || amount > max)
						throw new NumberFormatException(Localisation.getMessage(channel.getGuild(), "wrong-amount-dialog", min, max));
					return amount;
				});
	}
}
