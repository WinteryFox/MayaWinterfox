package com.winter.mayawinterfox.checks;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.permissions.Guild;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.function.Predicate;

public class PermissionChecks {

	public static Predicate<MessageReceivedEvent> hasPermission(Permissions p) {
		return e -> (e.getChannel().getModifiedPermissions(e.getAuthor()).contains(p) || e.getAuthor().getPermissionsForGuild(e.getGuild()).contains(Permissions.ADMINISTRATOR) || e.getGuild().getOwner().equals(e.getAuthor()) || Main.config.get(Main.ConfigValue.GLOBALS).contains(e.getAuthor().getStringID())) && e.getChannel().getModifiedPermissions(e.getClient().getOurUser()).contains(p);
	}

	public static Predicate<MessageReceivedEvent> hasPermission(Permissions p, boolean botRequiresPerm) {
		return e -> (!e.getChannel().getModifiedPermissions(e.getAuthor()).contains(p) && !e.getAuthor().getPermissionsForGuild(e.getGuild()).contains(Permissions.ADMINISTRATOR) && !e.getGuild().getOwner().equals(e.getAuthor()) && !Main.config.get(Main.ConfigValue.GLOBALS).contains(e.getAuthor().getStringID())) || !botRequiresPerm || e.getChannel().getModifiedPermissions(e.getClient().getOurUser()).contains(p);
	}

	public static Predicate<MessageReceivedEvent> isGlobal() {
		return e -> Main.config.get(Main.ConfigValue.GLOBALS).contains(e.getAuthor().getStringID());
	}

	public static boolean isGlobal(IUser user) {
		return Main.config.get(Main.ConfigValue.GLOBALS).contains(user.getStringID());
	}

	public static Predicate<MessageReceivedEvent> hasPermission(String permission) {
		return e -> {
			Guild guild = new Guild(e.getGuild());
			return guild.hasPermission(e.getAuthor(), permission);
		};
	}
}
