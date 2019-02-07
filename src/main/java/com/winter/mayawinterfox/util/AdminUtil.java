package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.TextChannel;


public class AdminUtil {

	/**
	 * Kicks a user from a guild
	 * @param event The event to kick for
	 * @return True on success, false on failure
	 */
	public static boolean kick(MessageCreateEvent event) {
		String[] args = MessageUtil.argsArray(event.getMessage());
		Member target;
		if (args.length > 1)
			target = ParsingUtil.getUser(args[1]).asMember(event.getGuildId().get()).block();
		else
			target = (Member) new TargetDialog((TextChannel) event.getMessage().getChannel().block(), event.getMember().get()).open();
		if (target == null)
			return false;
		String reason;
		if (args.length > 2)
			reason = args.length > 2 ? MessageUtil.args(event.getMessage()).substring(("kick " + args[1] + " ").length()) : null;
		else
			reason = (String) new InputDialog((TextChannel) event.getMessage().getChannel().block(), event.getMember().get(), "input-reason").open();
		event.getGuild().block().kick(target.getId(), reason);
		MessageUtil.sendMessage(event.getMessage().getChannel().block(), EmbedUtil.kickEmbed(event.getGuild().block(), target, event.getMember().get(), reason));
		return true;
	}

	/**
	 * Bans a user from a guild
	 * @param event The event to ban for
	 * @return True on success, false on failure
	 */
	public static boolean ban(MessageCreateEvent event) {
		String[] args = MessageUtil.argsArray(event.getMessage());
		Member target;
		if (args.length > 1)
			target = ParsingUtil.getUser(args[1]).asMember(event.getGuildId().get()).block();
		else
			target = (Member) new TargetDialog((TextChannel) event.getMessage().getChannel().block(), event.getMember().get()).open();
		if (target == null)
			return false;
		String reason;
		if (args.length > 2)
			reason = args.length > 2 ? MessageUtil.args(event.getMessage()).substring(("ban " + args[1] + " ").length()) : null;
		else
			reason = (String) new InputDialog(event.getMessage().getChannel().block(), event.getMember().get(), "input-reason").open();
		event.getGuild().block().ban(target.getId(), spec -> spec.setReason(reason));
		MessageUtil.sendMessage(event.getMessage().getChannel().block(), EmbedUtil.banEmbed(event.getGuild().block(), target, event.getMember().get(), reason));
		return true;
	}
}
