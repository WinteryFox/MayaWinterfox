package com.winter.mayawinterfox.data.permissions;

import com.winter.mayawinterfox.data.Database;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

public class Group {

	private final discord4j.core.object.entity.Guild guild;
	private String name;
	private final Set<String> permissions;
	private final Set<Member> members;
	private Snowflake roleID;
	
	Group(discord4j.core.object.entity.Guild guild, String name) {
		this.guild = guild;
		this.name = name;
		this.members = Database.get("SELECT userid FROM usergroup WHERE guild=? AND groupname=?;", guild.getId().asLong(), name)
				.flatMap(v -> guild.getMemberById(Snowflake.of((long) v.get("userid"))))
				.collect(Collectors.toSet())
				.block();
		this.permissions = Database.get("SELECT permission FROM groups WHERE guild=? AND groupname=?;", guild.getId().asLong(), name)
				.map(v -> (String) v.get("permission"))
				.collect(Collectors.toSet())
				.block();
		this.roleID = Snowflake.of((Long) Database.get("SELECT role FROM groups WHERE guild=? AND groupname=?;", guild.getId().asLong(), name)
				.blockFirst()
				.get("role"));
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public String getName() {
		return name;
	}

	public Set<Member> getMembers() {
		//if (members.isEmpty())
		//	members.addAll(Database.get("SELECT userid FROM usergroup WHERE guild=? AND groupname=?;", guild.getId().asLong(), name).stream().map(v -> guild.getMemberById(Snowflake.of((long) v.get("userid"))).block()).collect(Collectors.toSet()));
		//return members;
		return members;
	}

	public Mono<Group> addMember(discord4j.core.object.entity.User user) {
		return Database.set("INSERT IGNORE INTO usergroup (guild, userid, groupname) VALUES (?, ?, ?);", guild.getId().asLong(), user.getId().asLong(), name)
				.thenReturn(new Group(guild, name));
	}

	public Mono<Group> removeMember(discord4j.core.object.entity.User user) {
		return Database.set("DELETE FROM usergroup WHERE guild=? AND userid=? AND groupname=?", guild.getId().asLong(), user.getId().asLong(), name)
				.thenReturn(new Group(guild, name));
	}

	public Mono<Group> addPermission(String permission) {
		return Database.set("INSERT IGNORE INTO groups (guild, groupname, permission) VALUES (?, ?, ?);", guild.getId().asLong(), name, permission)
				.thenReturn(new Group(guild, name));
	}

	public Mono<Group> removePermission(String permission) {
		return Database.set("DELETE FROM groups WHERE guild=? AND groupname=? AND permission=?;", guild.getId().asLong(), name, permission)
				.thenReturn(new Group(guild, name));
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

	public Mono<Group> linkRole(Role role) {
		return Database.set("UPDATE groups SET role=? WHERE guild=? AND groupname=?;", role.getId().asLong(), guild.getId().asLong(), name)
				.thenReturn(new Group(guild, name));
	}

	public Role getRole() {
		if (roleID == null)
			return null;

		return guild.getRoleById(roleID).block();
	}
}
