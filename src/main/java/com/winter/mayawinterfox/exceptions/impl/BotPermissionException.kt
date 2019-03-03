package com.winter.mayawinterfox.exceptions.impl

import discord4j.core.`object`.util.PermissionSet

class BotPermissionException(set: PermissionSet) : Exception(set.joinToString())