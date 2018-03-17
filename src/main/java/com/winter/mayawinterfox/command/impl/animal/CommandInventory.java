package com.winter.mayawinterfox.command.impl.animal;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.animal.Wolf;
import com.winter.mayawinterfox.data.item.Item;
import com.winter.mayawinterfox.data.item.ItemProvider;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.ImageUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandInventory extends Node<Command> {

	public CommandInventory() {
		super(new Command(
				"inventory",
				"inventory-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {

					EmbedBuilder builder = new EmbedBuilder()
							.withColor(ColorUtil.withinTwoHues(0.333333f, 0.88888f))
							.withThumbnail(ImageUtil.getAvatar(e.getAuthor()))
							.withTitle(Localisation.getMessage(e.getGuild(), "inventory", e.getAuthor().getName()));
					for (ItemProvider.ItemType type : ItemProvider.ItemType.values()) {
						Set<Item> items = new Wolf(e.getAuthor()).getInventory().getItemsByType(type);
						if (!items.isEmpty())
							builder.appendField(StringUtils.capitalize(type.getTypeName()), Arrays.toString(items.stream().map(Item::getName).collect(Collectors.toList()).toArray()).replace("[", "").replace("]", ""), false);
						if (builder.getFieldCount() == 0)
							builder.withDescription(Localisation.getMessage(e.getGuild(), "empty-inventory"));
					}
					MessageUtil.sendMessage(e.getChannel(), builder.build());
					return true;
				}
		), Collections.emptyList());
	}
}
