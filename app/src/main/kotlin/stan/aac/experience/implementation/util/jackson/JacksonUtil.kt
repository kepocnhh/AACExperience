package stan.aac.experience.implementation.util.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.node.NullNode

fun JsonNode.require(key: String): JsonNode =
    get(key) ?: error("required property ($key) must be exist!")

fun JsonNode.isLikeNull(): Boolean = this is NullNode || nodeType == JsonNodeType.NULL || isNull

fun JsonNode.requireNotNull(key: String): JsonNode = require(key).apply {
    if (isLikeNull()) error("required property ($key) must be not null!")
}

fun JsonNode.asInteger(): Int {
    if(!isInt || !canConvertToInt()) error("property must be Int!")
    return intValue()
}

fun JsonNode.requireInt(key: String): Int = requireNotNull(key).asInteger()

fun JsonNode.asString(): String {
    if (!isTextual) error("property must be String!")
    return textValue() ?: error("property must be exist!")
}

fun JsonNode.requireString(key: String): String = requireNotNull(key).asString()

fun <T : Any> JsonNode.asList(transform: (JsonNode) -> T): List<T> {
    return if (!isArray) error("property must be Array!")
    else map(transform)
}

fun <T : Any> JsonNode.requireList(key: String, transform: (JsonNode) -> T): List<T> =
    requireNotNull(key).asList(transform)
