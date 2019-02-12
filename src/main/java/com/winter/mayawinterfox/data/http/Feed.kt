package com.winter.mayawinterfox.data.http

import discord4j.core.`object`.util.Snowflake

/**
 * An RSS Feed object
 */
data class Feed(val feed: String, val guild: Snowflake, val channel: Snowflake)