package com.winter.mayawinterfox.checks;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.function.Predicate;

public class ChannelChecks {

	public static Predicate<MessageReceivedEvent> isNSFW() {
		return messageReceivedEvent -> messageReceivedEvent.getChannel().isNSFW();
	}

}
