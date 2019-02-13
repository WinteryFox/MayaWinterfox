package com.winter.mayawinterfox.util

import com.fasterxml.jackson.xml.XmlMapper
import org.codehaus.jackson.map.DeserializationConfig
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

object Extensions {
    private val xmlMapper = XmlMapper()
            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun <T> InputStream.mapToXml(clazz: Class<T>) = xmlMapper.readValue(this, clazz)


    fun InputStream.asString(): String =
             try {
                BufferedReader(InputStreamReader(this)).use { buffer -> buffer.lines().collect(Collectors.joining("\n")) }
            } catch (ex: IOException) {
                ex.printStackTrace()
                ""

            }
}