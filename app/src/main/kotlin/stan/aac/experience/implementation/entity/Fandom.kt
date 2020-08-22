package stan.aac.experience.implementation.entity

import stan.aac.experience.foundation.entity.Fandom

private data class FandomImpl(
    override val id: String,
    override val name: String,
    override val domain: String
) : Fandom

fun fandom(
    id: String,
    name: String,
    domain: String
) : Fandom {
    return FandomImpl(
        id, name, domain
    )
}
