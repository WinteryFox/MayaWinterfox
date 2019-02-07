package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ColorUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.TextChannel;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PurgeDialog extends Dialog<String> {

	public PurgeDialog(TextChannel channel, Member user, Member target) {
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
						throw new NumberFormatException(Localisation.getMessage(channel.getGuild().block(), "wrong-amount"));
					//if (target != null)
						//channel.bulkDelete(channel.getMessageHistory(amount).stream().filter(m -> m.getAuthor().equals(target)).collect(Collectors.toList()));
						//channel.bulkDelete(channel.getMessagesBefore(channel.getLastMessageId().get()).publish(message -> {message.}))).blockFirst();
					//else
						//channel.bulkDelete(channel.getMessageHistoryTo(amount));
					// TODO: Fix
					return o;
				});
	}
}