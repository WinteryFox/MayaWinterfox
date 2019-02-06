package com.winter.mayawinterfox.data.permissions;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import discord4j.core.object.entity.Role;
import discord4j.core.object.util.Snowflake;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Group {

	private final discord4j.core.object.entity.Guild guild;
	private String name;
	private final Set<String> permissions;
	private final Set<discord4j.core.object.entity.User> members = new HashSet<>();
	private Long roleID;
	
	Group(discord4j.core.object.entity.Guild guild, String name) {
		this.guild = guild;
		this.name = name;
		this.permissions = Database.get("SELECT permission FROM groups WHERE guild=? AND groupname=?;", guild.getId().asLong(), name).stream().map(v -> (String) v.get("permission")).collect(Collectors.toSet());
		this.roleID = (Long) Database.get("SELECT role FROM groups WHERE guild=? AND groupname=?;", guild.getId().asLong(), name).get(0).get("role");
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public String getName() {
		return name;
	}

	public Set<discord4j.core.object.entity.User> getMembers() {
		if (members.isEmpty())
			members.addAll(Database.get("SELECT userid FROM usergroup WHERE guild=? AND groupname=?;", guild.getId().asLong(), name).stream().map(v -> guild.getMemberById(Snowflake.of((long) v.get("userid"))).block()).collect(Collectors.toSet()));
		return members;
	}

	public boolean addMember(discord4j.core.object.entity.User user) {
		if (!Database.set("INSERT IGNORE INTO usergroup (guild, userid, groupname) VALUES (?, ?, ?);", guild.getId().asLong(), user.getId().asLong(), name))
			throw new UpdateFailedException("Failed to update group in database.");
		return members.add(user);
	}

	public boolean removeMember(discord4j.core.object.entity.User user) {
		if (!Database.set("DELETE FROM usergroup WHERE guild=? AND userid=? AND groupname=?", guild.getId().asLong(), user.getId().asLong(), name))
			throw new UpdateFailedException("Failed to update group in database.");
		return members.remove(user);
	}

	public boolean addPermission(String permission) {
		if (!Database.set("INSERT IGNORE INTO groups (guild, groupname, permission) VALUES (?, ?, ?);", guild.getId().asLong(), name, permission))
			throw new UpdateFailedException("Failed to update group in database.");
		return permissions.add(permission);
	}

	public boolean removePermission(String permission) {
		if (!Database.set("DELETE FROM groups WHERE guild=? AND groupname=? AND permission=?;", guild.getId().asLong(), name, permission))
			throw new UpdateFailedException("Failed to update group in database.");
		return permissions.remove(permission);
	}

	public boolean hasPermission(String permission) {
		return hasPermission(new Node(permission));
	}

	public boolean hasPermission(Node node) {
		for (String s : permissions)
			if (new Node(s).test(node))
				return true;
		return false;
	}

	public void linkRole(Role role) {
		if (role == null)
			return;

		if (!Database.set("UPDATE groups SET role=? WHERE guild=? AND groupname=?;", role.getId().asLong(), guild.getId().asLong(), name))
			throw new UpdateFailedException("Failed to update group in database.");
		this.roleID = role.getId().asLong();
	}

	public Role getRole() {
		if (roleID == null)
			return null;

		return guild.getRoleById(Snowflake.of(roleID)).block();
	}
}
