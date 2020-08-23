package stan.aac.experience.implementation.provider.converter.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer
import com.fasterxml.jackson.databind.util.StdConverter
import stan.aac.experience.foundation.provider.converter.Converter

object JacksonConverter : Converter {
    override val from: Converter.From

    init {
        val objectMapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            val iterableSerializer = StdDelegatingSerializer(object: StdConverter<Iterable<*>?, Array<*>?>() {
                override fun convert(value: Iterable<*>?): Array<*>? {
                    return value?.toList()?.toTypedArray()
                }
            })
            val module = SimpleModule().apply {
                addSerializer(Iterable::class.java, iterableSerializer)
            }
            registerModule(module)
        }
        from = JacksonConverterFrom(objectMapper)
    }
}
