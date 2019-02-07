package com.winter.mayawinterfox.checks;

import com.winter.mayawinterfox.Main;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class PermissionChecks {

	public static Predicate<MessageCreateEvent> hasPermission(Permission p) {
		return event ->{
			Set<Permission> permissions = ((GuildChannel) event.getMessage().getChannel().block()).getEffectivePermissions(event.getMember().get().getId()).block();
			assert permissions != null;
			if (permissions.contains(p))
				return true;
			else if (permissions.contains(Permission.ADMINISTRATOR))
				return true;
			else if (event.getMember().get() == event.getGuild().block().getOwner().block())
				return true;
			else return Main.config.get(Main.ConfigValue.GLOBALS).contains(event.getMember().get().getId().asString());
		};
	}

	public static Predicate<MessageCreateEvent> isGlobal() {
		return e -> Main.config.get(Main.ConfigValue.GLOBALS).contains(e.getMember().get().getId().asString());
	}

	public static boolean isGlobal(User user) {
		return Main.config.get(Main.ConfigValue.GLOBALS).contains(user.getId().asString());
	}

	public static Predicate<MessageCreateEvent> hasPermission(String permission) {
		return e -> {
			//Guild guild = new Guild(e.getGuild().block().block());
			//return guild.hasPermission(e.getMember().get(), permission);
			return false;
		};
	}
}
