package com.winter.mayawinterfox.data.cache.meta;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.item.Item;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import java.sql.Date;

public class UserMeta {
	private final User user;
	private final String description;
	private final int level;
	private final int xp;
	private final int maxXP;
	private final long totalXP;
	private final int coins;
	private final int gems;
	private final Item background;
	private final boolean notifications;
	private final boolean premium;
	private final Date premiumExpiry;

	public UserMeta(User user, String description, int level, int xp, int maxXP, long totalXP, int coins, int gems, Item background, boolean notifications, boolean premium, Date premiumExpiry) {
		this.user = user;
		this.description = description;
		this.level = level;
		this.xp = xp;
		this.maxXP = maxXP;
		this.totalXP = totalXP;
		this.coins = coins;
		this.gems = gems;
		this.background = background;
		this.notifications = notifications;
		this.premium = premium;
		this.premiumExpiry = premiumExpiry;
	}

	public User getUser() {
		return user;
	}

	public String getDescription() {
		return description;
	}

	public Mono<UserMeta> setDescription(String description) {
		return Database.set("UPDATE user SET description=? WHERE id=?;", description, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public int getLevel() {
		return level;
	}

	public Mono<UserMeta> setLevel(int level) {
		return Database.set("UPDATE user SET level=? WHERE id=?;", level, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public int getXP() {
		return xp;
	}

	public Mono<UserMeta> setXp(int xp) {
		return Database.set("UPDATE user SET xp=? WHERE id=?;", xp, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public int getMaxXP() {
		return maxXP;
	}

	public Mono<UserMeta> setMaxXP(int maxXP) {
		return Database.set("UPDATE user SET maxxp=? WHERE id=?;", maxXP, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public long getTotalXP() {
		return totalXP;
	}

	public Mono<UserMeta> setTotalXP(long totalXP) {
		return Database.set("UPDATE user SET totalxp=? WHERE id=?;", totalXP, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public int getCoins() {
		return coins;
	}

	public Mono<UserMeta> setCoins(int coins) {
		return Database.set("UPDATE user SET coins=? WHERE id=?;", coins, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public int getGems() {
		return gems;
	}

	public Mono<UserMeta> setGems(int gems) {
		return Database.set("UPDATE user SET gems=? WHERE id=?;", gems, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public Item getBackground() {
		return background;
	}

	public Mono<UserMeta> setBackground(Item background) {
		return Database.set("UPDATE user SET background=? WHERE id=?;", background.getId(), user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public boolean isNotifications() {
		return notifications;
	}

	public Mono<UserMeta> setNotifications(boolean notifications) {
		return Database.set("UPDATE user SET lvlup=? WHERE id=?;", notifications, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public boolean isPremium() {
		return premium;
	}

	public Mono<UserMeta> setPremium(boolean premium) {
		return Database.set("UPDATE user SET premium=? WHERE id=?;", premium, user.getId().asLong())
				.then(Caches.getUser(user));
	}

	public Date getPremiumExpiry() {
		return premiumExpiry;
	}

	public Mono<UserMeta> setPremiumExpiry(Date premiumExpiry) {
		return Database.set("UPDATE user SET premiumexpiry=? WHERE id=?;", premiumExpiry, user.getId().asLong())
				.then(Caches.getUser(user));
	}
}