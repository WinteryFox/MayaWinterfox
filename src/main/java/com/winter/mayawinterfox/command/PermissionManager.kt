package com.winter.mayawinterfox.command

import discord4j.core.`object`.entity.TextChannel
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class PermissionManager(
		/**
		 * The permissions required by the bot to execute
		 * the command, e.g. SEND_MESSAGES or KICK_MEMBER
		 */
		val botPermissions: PermissionSet,

		/**
		 * The permissions required by the user to execute
		 * the command, same as above
		 */
		val userPermissions: PermissionSet
) {
	/**
	 * Test a member to determine whether or not the member
	 * has sufficient permissions to run the command
	 *
	 * @param event The event context
	 * @return A set returning all the missing permissions,
	 * if the set is empty all the permissions are present
	 */
	private fun testMember(event: MessageCreateEvent): Mono<PermissionSet> {
		return event.message.channel.ofType(TextChannel::class.java)
				.flatMap { c ->
					c.getEffectivePermissions(event.member.get().id).map { set ->
						userPermissions.subtract(set)
					}
				}
	}

	/**
	 * Test whether the bot has all the permissions required
	 * to execute the command
	 *
	 * @param event The event context
	 * @return A set returning all the missing permissions,
	 * if the set is empty all the permissions are present
	 */
	private fun testBot(event: MessageCreateEvent): Mono<PermissionSet> {
		return event.message.channel.ofType(TextChannel::class.java)
				.flatMap { c ->
					c.getEffectivePermissions(event.client.selfId.get()).map { set ->
						botPermissions.subtract(set)
					}
				}
	}
}