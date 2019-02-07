/*package com.winter.mayawinterfox.data.permissions;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.Row;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Guild {

	private final Guild guild;
	private final Map<String, Group> groups = new HashMap<>();
	private final Map<User, User> users = new HashMap<>();

	public Guild(Guild guild) {
		this.guild = guild;
		//Group d = new Group("maya.default");
		//for (Map.Entry<Commands.Category, List<com.winter.mayawinterfox.data.Node<Command>>> entry : Commands.COMMAND_MAP.entrySet())
		//	if (entry.getKey() != Commands.Category.ADMIN)
		//		for (com.winter.mayawinterfox.data.Node<Command> command : entry.getValue())
		//			d.addPermission(Commands.getPermission(command));
		//groups.put("maya.default", d);
		for (Row row : Database.get("SELECT groupname, permission FROM groups WHERE guild=?;", guild.getLongID())) {
			String name = (String) row.get("groupname");
			String permission = (String) row.get("permission");
			groups.computeIfPresent(name, (k, v) -> {
				v.addPermission(permission);
				return v;
			});
			groups.computeIfAbsent(name, k -> {
				Group group = new Group(guild, name);
				group.addPermission(permission);
				return group;
			});
		}
	}

	public boolean hasPermission(Member user, String permission) {
		//if (PermissionChecks.isGlobal(user))
		//	return true;
		//if (guild.getOwner().equals(user))
		//	return true;
		//if (user.getPermissionsForGuild(guild).contains(Permission.ADMINISTRATOR))
		//	return true;

		Node node = new Node(permission);
		for (Group g : groups.values()) {
			if (!g.hasPermission(node))
				continue;
			if (getUser(user).getGroups().contains(g.getName()))
				return true;
			if (g.getRole() != null)
				if (user.getRolesForGuild(guild).contains(g.getRole()))
					return true;
		}
		return getUser(user).hasPermission(node);
	}

	public User getUser(discord4j.core.object.entity.User user) {
		return users.computeIfAbsent(user, k -> new User(guild, user));
	}

	public Group getGroup(String group) {
		return groups.getOrDefault(group, null);
	}

	public void deleteGroup(String group, String permission) {
		if (!Database.set("DELETE FROM groups WHERE guild=? AND groupname=?;", guild.getLongID(), group))
			throw new UpdateFailedException("Failed to update guild in database.");
		groups.remove(group);
	}

	public Map<String, Group> hasGroup(String group) {
		return groups;
	}

	public Set<Group> getGroups() {
		return new HashSet<>(groups.values());
	}
}*/