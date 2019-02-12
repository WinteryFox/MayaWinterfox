package com.winter.mayawinterfox.data.http.bean

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class FeedBean(
		@JsonProperty("entry")
		var entry: List<Entry>?
) {
	constructor() : this(null)
}