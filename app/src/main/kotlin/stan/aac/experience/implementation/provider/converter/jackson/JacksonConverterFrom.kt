package stan.aac.experience.implementation.provider.converter.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.implementation.entity.fandom
import stan.aac.experience.foundation.provider.converter.Converter
import stan.aac.experience.implementation.util.jackson.requireInt
import stan.aac.experience.implementation.util.jackson.requireList
import stan.aac.experience.implementation.util.jackson.requireString

class JacksonConverterFrom(
    private val objectMapper: ObjectMapper
) : Converter.From {
    private fun fandom(root: JsonNode): Fandom {
        return fandom(
            id = root.requireInt("id"),
            name = root.requireString("name"),
            domain = root.requireString("domain")
        )
    }

    override fun fandoms(string: String): List<Fandom> {
        val root: JsonNode = objectMapper.readTree(string)
        return root.requireList("items", ::fandom)
    }
}
