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
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CommandInventory extends Node<Command> {

	public CommandInventory() {
		super(new Command(
				"inventory",
				"inventory-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					Consumer<EmbedCreateSpec> builder = spec -> spec
							.setColor(ColorUtil.withinTwoHues(0.333333f, 0.88888f))
							.setThumbnail(ImageUtil.getAvatar(e.getMember().get()))
							.setTitle(Localisation.getMessage(e.getGuild().block(), "inventory", e.getMember().get().getUsername()));
					for (ItemProvider.ItemType type : ItemProvider.ItemType.values()) {
						Set<Item> items = new Wolf(e.getMember().get()).getInventory().getItemsByType(type);
						if (!items.isEmpty())
							builder.andThen(spec -> spec.addField(StringUtils.capitalize(type.getTypeName()), Arrays.toString(items.stream().map(Item::getName).collect(Collectors.toList()).toArray()).replace("[", "").replace("]", ""), false));
						else
							builder.andThen(spec -> spec.setDescription(Localisation.getMessage(e.getGuild().block(), "empty-inventory")));
					}
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), builder);
					return true;
				}
		), Collections.emptyList());
	}
}
