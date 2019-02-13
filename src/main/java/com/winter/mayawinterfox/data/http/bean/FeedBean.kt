package com.winter.mayawinterfox.data.http.bean

import discord4j.core.`object`.util.Snowflake

data class FeedBean(val feed: String, val guild: Snowflake, val channel: Snowflake)