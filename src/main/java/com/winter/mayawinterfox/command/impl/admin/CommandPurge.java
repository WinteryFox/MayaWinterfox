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
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashSet;

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
						target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
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

					/*Flux<Snowflake> messages;
					Mono<MessageChannel> channel = e.getMessage().getChannel();
					//if (target != null)
					//	messages = channel
					//			.flatMapMany(c -> c.getMessagesBefore(e.getMessage().getId()))
					//			.map(Message::getId)
					//			.take(100);
					//else
						messages = channel
								.flux()
								.flatMap(c -> c.getMessagesBefore(e.getMessage().getId()))
								.map(m -> m.getId())
								.take(amount);*/

					Flux<Snowflake> history = e.getMessage().getChannel().ofType(TextChannel.class)
							.flatMapMany(c -> c.getMessagesBefore(e.getMessage().getId()))
							.filterWhen(m -> m.getAuthor().map(u ->
									target == null || u.equals(target)
							))
							.filter(m -> m.getTimestamp().isAfter(LocalDate.now().minusWeeks(2).atStartOfDay().toInstant(ZoneOffset.UTC)))
							.take(amount)
							.map(Message::getId)
							.cache();
					Long count = e.getMessage().getChannel().ofType(TextChannel.class)
							.map(c -> c.bulkDelete(history)
									.count()
									.flatMap(not -> history.count().map(total -> total - not)).block())
							.block();

					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "purged-messages", count));
					return true;
				},
				new HashSet<>(Collections.singletonList("prune"))
		), Collections.emptyList());
	}
}