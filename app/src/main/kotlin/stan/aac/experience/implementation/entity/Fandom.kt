package stan.aac.experience.implementation.entity

import stan.aac.experience.foundation.entity.Fandom

private data class FandomImpl(
    override val id: Int,
    override val name: String,
    override val domain: String
) : Fandom

fun fandom(
    id: Int,
    name: String,
    domain: String
) : Fandom {
    return FandomImpl(
        id = id,
        name = name,
        domain = domain
    )
}
