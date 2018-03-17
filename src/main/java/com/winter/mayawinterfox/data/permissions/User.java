package com.winter.mayawinterfox.data.permissions;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import org.eclipse.jetty.util.ConcurrentHashSet;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

import java.util.Set;
import java.util.stream.Collectors;

public class User {

	private final IGuild guild;
	private final IUser user;
	private final Set<String> groups = new ConcurrentHashSet<>();
	private final Set<String> permissions = new ConcurrentHashSet<>();

	public User(IGuild guild, IUser user) {
		this.guild = guild;
		this.user = user;
		this.permissions.addAll(Database.get("SELECT permission FROM permission WHERE userid=? AND guild=?;", user.getLongID(), guild.getLongID()).stream().map(v -> (String) v.get("permission")).collect(Collectors.toSet()));
		groups.addAll(Database.get("SELECT groupname FROM usergroup WHERE guild=? AND userid=?;", guild.getLongID(), user.getLongID()).stream().map(v -> (String) v.get("groupname")).collect(Collectors.toSet()));
		groups.add("maya.default");
	}

	public boolean hasPermission(Node node) {
		for (String s : permissions)
			if (new Node(s).test(node))
				return true;
		return false;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public boolean addGroup(Group group) {
		if (!Database.set("INSERT IGNORE INTO usergroup (guild, userid, groupname) VALUES (?, ?, ?);", guild.getLongID(), user.getLongID(), group.getName()))
			throw new UpdateFailedException("Failed to update user permissions.");
		return groups.add(group.getName());
	}

	public boolean removeGroup(Group group) {
		if (!Database.set("DELETE FROM usergroup WHERE guild=? AND userid=? AND groupname=?", guild.getLongID(), user.getLongID(), group.getName()))
			throw new UpdateFailedException("Failed to update user permissions.");
		return groups.remove(group.getName());
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public boolean addPermission(String permission) {
		if (!Database.set("INSERT IGNORE INTO permission (guild, userid, permission) VALUES (?, ?, ?);", guild.getLongID(), user.getLongID(), permission))
			throw new UpdateFailedException("Failed to update user permissions.");
		return permissions.add(permission);
	}

	public boolean removePermission(String permission) {
		if (!Database.set("DELETE FROM permission WHERE guild=? AND userid=? AND permission=?", guild.getLongID(), user.getLongID(), permission))
			throw new UpdateFailedException("Failed to update user permissions.");
		return permissions.remove(permission);
	}
}
