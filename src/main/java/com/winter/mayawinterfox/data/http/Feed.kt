package com.winter.mayawinterfox.data.http

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
        val entry: List<Entry?>? = null
) {
    constructor() : this(null)
}

data class Entry(
        val author: Author? = null,
        val category: Category? = null,
        val content: String? = null,
        val id: String? = null,
        val updated: String? = null,
        val title: String? = null
) {
    constructor() : this(null)
}

data class Author(val name: String? = null, val uri: String? = null) {
    constructor() : this(null)
}


data class Category(val term: String? = null, val label: String? = null) {
    constructor() : this(null)
}