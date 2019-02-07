package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.AmountDialog;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class CommandPurge extends Node<Command> {

	public CommandPurge() {
		super(new Command(
				"purge",
				"purge-help",
				PermissionChecks.hasPermission(Permission.MANAGE_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					Member target;
					int amount;
					if (args.length > 1) {
						amount = Integer.parseUnsignedInt(args[1]);
						if (amount == 0)
							throw new NumberFormatException(Localisation.getMessage(e.getGuild().block(), "wrong-amount-dialog", 1, 100));
					} else {
						try {
							amount = new AmountDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get(), 1, 100).open();
						} catch (NullPointerException ex) {
							return false;
						}
					}
					if (amount == 0)
						return false;
					if (args.length > 2)
						target = ParsingUtil.getUser(args[2]).asMember(e.getGuildId().get()).block();
					else
						target = new TargetDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get()).open();
					/*if (target != null)
						history = e.getMessage().getChannel().block().getMessageHistory(amount).stream().filter(m -> m.getAuthor().equals(target)).collect(Collectors.toList());
					else
						history = e.getMessage().getChannel().block().getMessageHistory(amount);
					GuildUtil.bulkDelete(e.getMessage().getChannel().block(), history);*/

					/*Flux<Message> messages;
					if (target != null)
						messages = e.getMessage().getChannel().block().getMessagesBefore(e.getMessage().getId()).take(amount);
					else
						messages = e.getMessage().getChannel().block().getMessagesBefore(e.getMessage().getId()).take(amount).filter(message -> {

						});*/

					Publisher<Snowflake> messages;
					if (target != null)
						messages = message -> {
							e.getMessage().getChannel().block().getMessagesBefore(e.getMessage().getId()).take(100);
						};
					else
						messages = m -> {
							e.getMessage().getChannel().block().getMessagesBefore(e.getMessage().getId()).filter(message -> Objects.equals(message.getAuthor().block(), target));
						};

					((TextChannel) e.getMessage().getChannel().block()).bulkDelete(messages);

					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "purged-messages", "?"));
					return true;
				},
				new HashSet<>(Collections.singletonList("prune"))
		), Collections.emptyList());
	}
}