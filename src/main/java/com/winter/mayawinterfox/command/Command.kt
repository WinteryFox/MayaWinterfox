package com.winter.mayawinterfox.command;

import com.winter.mayawinterfox.command.Command.Category
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import java.util.function.Consumer

class Command(
		/**
		 * Name of the command
		 */
		val name: String,

		/**
		 * The help message key in the localization file for the necessary translations
		 */
		val help: String,

		/**
		 * The category of the command
		 * @see Category
		 */
		val category: Category,

		/**
		 * The permission manager for this command, checks bot & user permissions
		 * before execution of the command
		 * @see PermissionManager
		 */
		val manager: PermissionManager,

		/**
		 * The actual command call, where the command is actually executed
		 */
		private val call: Consumer<MessageCreateEvent>,

		/**
		 * Aliases for the command
		 */
		val aliases: Set<String> = emptySet()
) {
	fun call(event: MessageCreateEvent): Mono<Void> {
		return Mono.fromCallable { call.accept(event) }.then()
	}

	/**
	 * All the command categories and their labels
	 */
	enum class Category(val type: String) {
		ADMIN("admin"),
		FUN("fun"),
		DEV("developer"),
		STATUS("status"),
		MISC("miscellaneous"),
		ANIMAL("animal"),
		PROFILE("profile"),
		UTIL("utility"),
		MUSIC("music"),
		IMAGE("image")
	}
}