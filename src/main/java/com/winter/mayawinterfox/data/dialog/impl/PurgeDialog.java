package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ColorUtil;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PurgeDialog extends Dialog<String> {

	public PurgeDialog(IChannel channel, IUser user, IUser target) {
		super(DialogType.OPEN,
				channel,
				user,
				null,
				"ask-message-purge",
				new Object[0],
				"input-purge-number",
				new Object[0],
				null,
				ColorUtil.withinTwoHues(0.0833333F, 0.20833333F),
				1L,
				TimeUnit.MINUTES,
				o -> {
					if (o == null)
						return null;
					int amount = Integer.parseUnsignedInt(o);
					if (amount <= 0 || amount > 100)
						throw new NumberFormatException(Localisation.getMessage(channel.getGuild(), "wrong-amount"));
					if (target != null)
						channel.bulkDelete(channel.getMessageHistory(amount).stream().filter(m -> m.getAuthor().equals(target)).collect(Collectors.toList()));
					else
						channel.bulkDelete(channel.getMessageHistoryTo(amount));
					return o;
				});
	}
}