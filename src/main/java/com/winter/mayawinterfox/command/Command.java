package com.winter.mayawinterfox.command;

import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class Command {

	private final String name;
	private final String help;
	private final Predicate<MessageCreateEvent> check;
	private final Predicate<MessageCreateEvent> call;
	private final Set<String> aliases;

	/**
	 * Creates a new command class
	 * @param name  Name of the command or sub-command.
	 * @param help  The help message key of the command.
	 * @param check Whether to run the call or not.
	 * @param call  Run the call, false means show help message. Help is determined by <code>[name]-help</code>.
	 */
	public Command(String name, String help, Predicate<MessageCreateEvent> check, Predicate<MessageCreateEvent> call) {
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
	public Command(String name, String help, Predicate<MessageCreateEvent> check, Predicate<MessageCreateEvent> call, Set<String> aliases) {
		this.name = name;
		this.help = help;
		this.check = check;
		this.call = call;
		this.aliases = new HashSet<>(aliases);
		this.aliases.add(name);
	}

	public void call(MessageCreateEvent e) {
		call.test(e);
	}

	public String getName() {
		return name;
	}

	public String getHelp() {
		return help;
	}

	public Predicate<MessageCreateEvent> getCheck() { return check; }

	public Set<String> getAliases() {
		return aliases;
	}

}
