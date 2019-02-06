package com.winter.mayawinterfox.util;

import com.winter.mayawinterfox.data.locale.Localisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.time.ZoneId;

public class StatusUtil {

	public static final Logger LOGGER = LoggerFactory.getLogger(StatusUtil.class);

	public static boolean ping(MessageReceivedEvent e) {
		long gatewayPing = e.getClient().getOurUser().getShard().getResponseTime();
		long messageSent = e.getMessage().getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		LOGGER.debug("Message sent at `" + messageSent + "`...");
		long beforeSend = System.currentTimeMillis();
		IMessage m = e.getMessage().getChannel().block().sendMessage(Localisation.of(e.getGuild().block(), "receive-latency", (beforeSend - messageSent)));
		long afterSend = System.currentTimeMillis();
		m.edit(Localisation.of(e.getGuild().block(), "receive-latency", (beforeSend - messageSent)) + "\n" + Localisation.of(e.getGuild().block(), "send-latency", (afterSend - beforeSend)));
		long afterEdit = System.currentTimeMillis();
		EmbedBuilder eb = new EmbedBuilder();
		eb.withColor(Color.getHSBColor(((float) (afterEdit - messageSent)) / 360.0f, 1.0f, 1.0f));
		LOGGER.debug(String.format("Hue of Ping embed: %f", ((float) (afterEdit - messageSent)) / 360.0f));
		eb.appendDescription(Localisation.of(e.getGuild().block(), "message-timestamp",messageSent) + "\n");
		eb.appendDescription(Localisation.of(e.getGuild().block(), "before-send", beforeSend) + "\n");
		eb.appendDescription(Localisation.of(e.getGuild().block(), "after-send", afterSend) + "\n");
		eb.appendDescription(Localisation.of(e.getGuild().block(), "after-edit", afterEdit) + "\n");
		eb.appendDescription(Localisation.of(e.getGuild().block(), "gateway-response-time", gatewayPing));
		m.edit(Localisation.of(e.getGuild().block(), "receive-latency", (beforeSend - messageSent)) + "\n" + Localisation.of(e.getGuild().block(), "send-latency", (afterSend - beforeSend)) + "\n" + Localisation.of(e.getGuild().block(), "edit-latency", (afterEdit - afterSend)), eb.build());
		return true;
	}
}
