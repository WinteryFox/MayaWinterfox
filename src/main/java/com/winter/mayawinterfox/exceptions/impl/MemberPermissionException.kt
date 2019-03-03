package com.winter.mayawinterfox.exceptions.impl

import discord4j.core.`object`.util.PermissionSet

class MemberPermissionException(set: PermissionSet) : Exception(set.joinToString())