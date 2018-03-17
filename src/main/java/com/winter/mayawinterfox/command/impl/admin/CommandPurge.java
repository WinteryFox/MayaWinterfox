package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.AmountDialog;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.GuildUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CommandPurge extends Node<Command> {

	public CommandPurge() {
		super(new Command(
				"purge",
				"purge-help",
				PermissionChecks.hasPermission(Permissions.MANAGE_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IUser target;
					int amount;
					if (args.length > 1) {
						amount = Integer.parseUnsignedInt(args[1]);
						if (amount == 0)
							throw new NumberFormatException(Localisation.getMessage(e.getGuild(), "wrong-amount-dialog", 1, 100));
					} else {
						try {
							amount = (int) new AmountDialog(e.getChannel(), e.getAuthor(), 1, 100).open();
						} catch (NullPointerException ex) {
							return false;
						}
					}
					if (amount == 0)
						return false;
					if (args.length > 2)
						target = ParsingUtil.getUser(args[2]);
					else
						target = (IUser) new TargetDialog(e.getChannel(), e.getAuthor()).open();
					List<IMessage> history;
					if (target != null)
						history = e.getChannel().getMessageHistory(amount).stream().filter(m -> m.getAuthor().equals(target)).collect(Collectors.toList());
					else
						history = e.getChannel().getMessageHistory(amount);
					GuildUtil.bulkDelete(e.getChannel(), history);
					MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "purged-messages", history.size()));
					return true;
				},
				new HashSet<>(Collections.singletonList("prune"))
		), Collections.emptyList());
	}
}