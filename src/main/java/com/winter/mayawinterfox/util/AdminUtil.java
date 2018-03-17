package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;


public class AdminUtil {

	/**
	 * Kicks a user from a guild
	 * @param event The event to kick for
	 * @return True on success, false on failure
	 */
	public static boolean kick(MessageReceivedEvent event) {
		String[] args = MessageUtil.argsArray(event.getMessage());
		IUser target;
		if (args.length > 1)
			target = ParsingUtil.getUser(args[1]);
		else
			target = (IUser) new TargetDialog(event.getChannel(), event.getAuthor()).open();
		if (target == null)
			return false;
		String reason;
		if (args.length > 2)
			reason = args.length > 2 ? MessageUtil.args(event.getMessage()).substring(("kick " + args[1] + " ").length()) : null;
		else
			reason = (String) new InputDialog(event.getChannel(), event.getAuthor(), "input-reason").open();
		event.getGuild().kickUser(target, reason);
		MessageUtil.sendMessage(event.getChannel(), EmbedUtil.kickEmbed(event.getGuild(), target, event.getAuthor(), reason));
		return true;
	}

	/**
	 * Bans a user from a guild
	 * @param event The event to ban for
	 * @return True on success, false on failure
	 */
	public static boolean ban(MessageReceivedEvent event) {
		String[] args = MessageUtil.argsArray(event.getMessage());
		IUser target;
		if (args.length > 1)
			target = ParsingUtil.getUser(args[1]);
		else
			target = (IUser) new TargetDialog(event.getChannel(), event.getAuthor()).open();
		if (target == null)
			return false;
		String reason;
		if (args.length > 2)
			reason = args.length > 2 ? MessageUtil.args(event.getMessage()).substring(("ban " + args[1] + " ").length()) : null;
		else
			reason = (String) new InputDialog(event.getChannel(), event.getAuthor(), "input-reason").open();
		event.getGuild().banUser(target, reason);
		MessageUtil.sendMessage(event.getChannel(), EmbedUtil.banEmbed(event.getGuild(), target, event.getAuthor(), reason));
		return true;
	}
}
