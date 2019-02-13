package com.winter.mayawinterfox.data.http

import org.codehaus.jackson.annotate.JsonProperty
import java.util.*

/**
 * An RSS Feed object
 */
data class Feed(
        val category: Category? = null,
        val updated: String? = null,
        val icon: String? = null,
        val id: String? = null,
        val logo: String? = null,
        val title: String? = null,
        val subtitle: String? = null,
        @JsonProperty("entry")
        val entries: Array<Entry?>? = null
) {
    constructor() : this(null)

    override fun toString(): String {
        return """
            Feed(category=$category,updated=$updated,icon=$icon,id=$id,logo=$logo,title=$title,subtitle=$subtitle,entries=${Arrays.toString(entries)}]
        """.trimIndent()
    }
}

data class Entry(
        val author: Author? = null,
        val category: Category? = null,
        val content: Content? = null,
        val id: String? = null,
        val updated: String? = null,
        val title: String? = null
) {
    constructor() : this(null)
}

data class Content(
        val type: String? = null
) {
    constructor() : this(null)
}

data class Author(val name: String? = null, val uri: String? = null) {
    constructor() : this(null)
}


data class Category(val term: String? = null, val label: String? = null) {
    constructor() : this(null)
}