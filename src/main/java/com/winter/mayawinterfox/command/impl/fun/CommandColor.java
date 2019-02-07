package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

public class CommandColor extends Node<Command> {

	public CommandColor() {
		super(new Command(
				"color",
				"color-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				CommandColor::displayColor
		), Collections.emptyList());
	}

	private static boolean displayColor(MessageCreateEvent event) {
		String cs = event.getMessage().getContent().get().substring(event.getMessage().getContent().get().lastIndexOf(' ')).replaceFirst("#", "").trim();
		Color c = new Color(Integer.parseInt(cs, 16));
		BufferedImage bi = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, 256, 256);
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "png", b);
		} catch (IOException e) {
			ErrorHandler.log(e, "Writing color to OutputStream");
		}
		ByteArrayInputStream i = new ByteArrayInputStream(b.toByteArray());
		MessageUtil.sendMessage((TextChannel) event.getMessage().getChannel().block(), "color.png", i);
		try {
			b.close();
			i.close();
		} catch (IOException e) {
			ErrorHandler.log(e, "stream-closure");
		}
		return true;
	}

}
