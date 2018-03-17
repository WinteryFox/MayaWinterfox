package com.winter.mayawinterfox.command;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class Command {

	private final String name;
	private final String help;
	private final Predicate<MessageReceivedEvent> check;
	private final Predicate<MessageReceivedEvent> call;
	private final Set<String> aliases;

	/**
	 * Creates a new command class
	 * @param name  Name of the command or sub-command.
	 * @param help  The help message key of the command.
	 * @param check Whether to run the call or not.
	 * @param call  Run the call, false means show help message. Help is determined by <code>[name]-help</code>.
	 */
	public Command(String name, String help, Predicate<MessageReceivedEvent> check, Predicate<MessageReceivedEvent> call) {
		this(name, help, check, call, new HashSet<>());
	}

	/**
	 * Creates a new command class
	 * @param name  Name of the command or sub-command.
	 * @param help  The help message key of the command.
	 * @param check Whether to run the call or not.
	 * @param call  Run the call, false means show help message. Help is determined by <code>[name]-help</code>.
	 * @param aliases The aliases for the command
	 */
	public Command(String name, String help, Predicate<MessageReceivedEvent> check, Predicate<MessageReceivedEvent> call, Set<String> aliases) {
		this.name = name;
		this.help = help;
		this.check = check;
		this.call = call;
		this.aliases = new HashSet<>(aliases);
		this.aliases.add(name);
	}

	public void call(MessageReceivedEvent e) {
		call.test(e);
	}

	public String getName() {
		return name;
	}

	public String getHelp() {
		return help;
	}

	public Predicate<MessageReceivedEvent> getCheck() { return check; }

	public Set<String> getAliases() {
		return aliases;
	}

}
