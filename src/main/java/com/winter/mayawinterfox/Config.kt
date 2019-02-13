package com.winter.mayawinterfox

import discord4j.core.`object`.util.Snowflake
import java.io.InputStream
import java.util.*

data class Config(
	val prefix: String,
	val debug: Boolean,
	val token: String,
	val debugToken: String,
	val errorChannel: Snowflake,
	val databaseIp: String,
	val databaseUser: String,
	val databaseSchema: String,
	val databasePassword: String,
	val databasePoolSize: Int,
	val globals: Set<Snowflake>,
	val botsPwToken: String,
	val botsOrgToken: String,
	val malCredentials: String,
	val mashapeToken: String,
	val youtubeToken: String,
	val maxPrefixes: Int,
	val premiumMaxPrefixes: Int,
	val maxAutoRoles: Int,
	val premiumMaxAutoRoles: Int,
	val maxSongQueue: Int,
	val premiumMaxSongQueue: Int
) {
	//constructor(val stream: InputStream) {
	//	val properties: Properties = Properties()
	//	properties.load(stream)


	//}
}