package com.winter.mayawinterfox.data.http.bean

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Entry(
		@JsonProperty("content")
		var content: String?,
		@JsonProperty("id")
		var id: String?,
		@JsonProperty("link")
		var link: String?,
		@JsonProperty("title")
		var title: String?
) {
	constructor() : this("", "", "", "")
}