package com.winter.mayawinterfox.command

import discord4j.core.`object`.util.PermissionSet

class BotPermissionException(set: PermissionSet) : Exception(set.joinToString())