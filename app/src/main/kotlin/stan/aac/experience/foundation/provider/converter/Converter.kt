package stan.aac.experience.foundation.provider.converter

import stan.aac.experience.foundation.entity.Fandom

interface Converter {
    interface From {
//        fun fandom(string: String): Fandom
        fun fandoms(string: String): List<Fandom>
    }
//    interface To {}

    val from: From
//    val to: To
}
