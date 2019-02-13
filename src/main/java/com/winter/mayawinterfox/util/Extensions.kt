package com.winter.mayawinterfox.util

import com.fasterxml.jackson.xml.XmlMapper
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.map.DeserializationConfig
import org.codehaus.jackson.map.ObjectMapper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

object Extensions {
    private val xmlMapper = XmlMapper()
            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationConfig.Feature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
            .configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)

    private val mapper = ObjectMapper()
            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)

    fun <T> String.mapToXml(clazz: Class<T>) = xmlMapper.readValue(this, clazz)

    fun  String.asJsonFromXml(): JsonNode = xmlMapper.readTree(this)

    fun JsonNode.asJsonString(): String = mapper.writeValueAsString(this)

    fun <T> String.`as`(clazz: Class<T>): T = mapper.readValue(this, clazz)


    fun InputStream.asString(): String =
             try {
                BufferedReader(InputStreamReader(this)).use { buffer -> buffer.lines().collect(Collectors.joining("\n")) }
            } catch (ex: IOException) {
                ex.printStackTrace()
                ""

            }
}