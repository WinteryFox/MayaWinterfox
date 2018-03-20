package com.winter.mayawinterfox.data.permissions;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Group {

	private final IGuild guild;
	private String name;
	private final Set<String> permissions;
	private final Set<IUser> members = new HashSet<>();
	private Long roleID;
	
	Group(IGuild guild, String name) {
		this.guild = guild;
		this.name = name;
		this.permissions = Database.get("SELECT permission FROM groups WHERE guild=? AND groupname=?;", guild.getLongID(), name).stream().map(v -> (String) v.get("permission")).collect(Collectors.toSet());
		this.roleID = (Long) Database.get("SELECT role FROM groups WHERE guild=? AND groupname=?;", guild.getLongID(), name).get(0).get("role");
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public String getName() {
		return name;
	}

	public Set<IUser> getMembers() {
		if (members.isEmpty())
			members.addAll(Database.get("SELECT userid FROM usergroup WHERE guild=? AND groupname=?;", guild.getLongID(), name).stream().map(v -> guild.getUserByID((long) v.get("userid"))).collect(Collectors.toSet()));
		return members;
	}

	public boolean addMember(IUser user) {
		if (!Database.set("INSERT IGNORE INTO usergroup (guild, userid, groupname) VALUES (?, ?, ?);", guild.getLongID(), user.getLongID(), name))
			throw new UpdateFailedException("Failed to update group in database.");
		return members.add(user);
	}

	public boolean removeMember(IUser user) {
		if (!Database.set("DELETE FROM usergroup WHERE guild=? AND userid=? AND groupname=?", guild.getLongID(), user.getLongID(), name))
			throw new UpdateFailedException("Failed to update group in database.");
		return members.remove(user);
	}

	public boolean addPermission(String permission) {
		if (!Database.set("INSERT IGNORE INTO groups (guild, groupname, permission) VALUES (?, ?, ?);", guild.getLongID(), name, permission))
			throw new UpdateFailedException("Failed to update group in database.");
		return permissions.add(permission);
	}

	public boolean removePermission(String permission) {
		if (!Database.set("DELETE FROM groups WHERE guild=? AND groupname=? AND permission=?;", guild.getLongID(), name, permission))
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

	public void linkRole(IRole role) {
		if (role == null)
			return;

		if (!Database.set("UPDATE groups SET role=? WHERE guild=? AND groupname=?;", role.getLongID(), guild.getLongID(), name))
			throw new UpdateFailedException("Failed to update group in database.");
		this.roleID = role.getLongID();
	}

	public IRole getRole() {
		if (roleID == null)
			return null;

		return guild.getRoleByID(roleID);
	}
}
