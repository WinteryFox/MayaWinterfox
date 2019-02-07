package com.winter.mayawinterfox.data.permissions;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class User {

	private final Guild guild;
	private final Member user;
	private final Set<String> groups = new HashSet<>();
	private final Set<String> permissions = new HashSet<>();

	public User(Guild guild, Member user) {
		this.guild = guild;
		this.user = user;
		this.permissions.addAll(Database.get("SELECT permission FROM permission WHERE userid=? AND guild=?;", user.getId().asLong(), guild.getId().asLong()).stream().map(v -> (String) v.get("permission")).collect(Collectors.toSet()));
		groups.addAll(Database.get("SELECT groupname FROM usergroup WHERE guild=? AND userid=?;", user.getId().asLong(), user.getId().asLong()).stream().map(v -> (String) v.get("groupname")).collect(Collectors.toSet()));
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
		if (!Database.set("INSERT IGNORE INTO usergroup (guild, userid, groupname) VALUES (?, ?, ?);", guild.getId().asLong(), user.getId().asLong(), group.getName()))
			throw new UpdateFailedException("Failed to update user permissions.");
		return groups.add(group.getName());
	}

	public boolean removeGroup(Group group) {
		if (!Database.set("DELETE FROM usergroup WHERE guild=? AND userid=? AND groupname=?", guild.getId().asLong(), user.getId().asLong(), group.getName()))
			throw new UpdateFailedException("Failed to update user permissions.");
		return groups.remove(group.getName());
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public boolean addPermission(String permission) {
		if (!Database.set("INSERT IGNORE INTO permission (guild, userid, permission) VALUES (?, ?, ?);", guild.getId().asLong(), user.getId().asLong(), permission))
			throw new UpdateFailedException("Failed to update user permissions.");
		return permissions.add(permission);
	}

	public boolean removePermission(String permission) {
		if (!Database.set("DELETE FROM permission WHERE guild=? AND userid=? AND permission=?", guild.getId().asLong(), user.getId().asLong(), permission))
			throw new UpdateFailedException("Failed to update user permissions.");
		return permissions.remove(permission);
	}
}
