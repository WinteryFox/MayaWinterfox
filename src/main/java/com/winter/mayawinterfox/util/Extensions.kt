package com.winter.mayawinterfox.util

import com.fasterxml.jackson.xml.XmlMapper
import org.codehaus.jackson.map.DeserializationConfig
import java.io.InputStream

object Extensions {
    private val xmlMapper = XmlMapper()
            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun <T> InputStream.mapToXml(clazz: Class<T>) = xmlMapper.readValue(this, clazz)
}