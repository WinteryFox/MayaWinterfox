package com.winter.mayawinterfox.checks;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.TextChannel;

import java.util.function.Predicate;

public class ChannelChecks {

	public static Predicate<MessageCreateEvent> isNSFW() {
		return messageReceivedEvent -> ((TextChannel) messageReceivedEvent.getMessage().getChannel().block()).isNsfw();
	}

}
