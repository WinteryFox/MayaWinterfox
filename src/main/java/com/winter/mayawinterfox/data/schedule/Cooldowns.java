package com.winter.mayawinterfox.data.schedule;

import discord4j.core.object.entity.Member;

import java.util.concurrent.ConcurrentHashMap;

public class Cooldowns {

	private static final ConcurrentHashMap<Long, ConcurrentHashMap<String, Long>> COOLDOWNS = new ConcurrentHashMap<>();

	/**
	 * Check if a user's bucket is still on cool-down
	 * @param user The user to getGuild the bucket for
	 * @param bucket The unique bucket
	 * @return True if still on cool-down, false if not
	 */
	public static boolean onCooldown(Member user, String bucket) {
		ConcurrentHashMap<String, Long> bket = COOLDOWNS.computeIfAbsent(user.getId().asLong(), d -> new ConcurrentHashMap<>());
		return bket.getOrDefault(bucket, 0L) > System.currentTimeMillis();
	}

	/**
	 * Get the remaining time left on a user's bucket
	 * @param user The user to getGuild the bucket for
	 * @param bucket The unique bucket
	 * @return The remaining time for a cool-down
	 */
	public static long getRemaining(Member user, String bucket) {
		return getCooldownEndTime(user, bucket) - System.currentTimeMillis();
	}

	/**
	 * Get the end time for a cool-down
	 * @param bucket The unique bucket
	 * @param user The user to getGuild the bucket for
	 * @return The end time in system time
	 */
	private static long getCooldownEndTime(Member user, String bucket) {
		ConcurrentHashMap<String, Long> bket = COOLDOWNS.computeIfAbsent(user.getId().asLong(), d -> new ConcurrentHashMap<>());
		return bket.getOrDefault(bucket, 0L);
	}

	/**
	 * Put a bucket on cool-down for someone
	 * @param bucket The unique bucket
	 * @param user The user to getGuild the bucket for
	 * @param cooldown The cool-down length
	 */
	public static void putOnCooldown(Member user, String bucket, long cooldown) {
		ConcurrentHashMap<String, Long> bket = COOLDOWNS.computeIfAbsent(user.getId().asLong(), d -> new ConcurrentHashMap<>());
		bket.put(bucket, System.currentTimeMillis() + cooldown);
	}
}