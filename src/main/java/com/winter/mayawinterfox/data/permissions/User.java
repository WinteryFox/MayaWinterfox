package com.winter.mayawinterfox.data.permissions;

import com.winter.mayawinterfox.data.Database;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

public class User {

	private final Guild guild;
	private final Member user;
	private final Set<String> groups = new HashSet<>();
	private final Set<String> permissions = new HashSet<>();

	public User(Guild guild, Member user) {
		this.guild = guild;
		this.user = user;
		//this.permissions.addAll(Database.get("SELECT permission FROM permission WHERE userid=? AND guild=?;", user.getId().asLong(), guild.getId().asLong()).stream().map(v -> (String) v.get("permission")).collect(Collectors.toSet()));
		//groups.addAll(Database.get("SELECT groupname FROM usergroup WHERE guild=? AND userid=?;", user.getId().asLong(), user.getId().asLong()).stream().map(v -> (String) v.get("groupname")).collect(Collectors.toSet()));
		// TODO
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

	public Mono<User> addGroup(Group group) {
		return Database.set("INSERT IGNORE INTO usergroup (guild, userid, groupname) VALUES (?, ?, ?);", guild.getId().asLong(), user.getId().asLong(), group.getName())
				.thenReturn(new User(guild, user));
	}

	public Mono<User> removeGroup(Group group) {
		return Database.set("DELETE FROM usergroup WHERE guild=? AND userid=? AND groupname=?", guild.getId().asLong(), user.getId().asLong(), group.getName())
				.thenReturn(new User(guild, user));
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public Mono<User> addPermission(String permission) {
		return Database.set("INSERT IGNORE INTO permission (guild, userid, permission) VALUES (?, ?, ?);", guild.getId().asLong(), user.getId().asLong(), permission)
				.thenReturn(new User(guild, user));
	}

	public Mono<User> removePermission(String permission) {
		return Database.set("DELETE FROM permission WHERE guild=? AND userid=? AND permission=?", guild.getId().asLong(), user.getId().asLong(), permission)
				.thenReturn(new User(guild, user));
	}
}
