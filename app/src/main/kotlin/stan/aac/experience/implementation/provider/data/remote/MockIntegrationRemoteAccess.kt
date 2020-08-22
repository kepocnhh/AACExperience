package stan.aac.experience.implementation.provider.data.remote

import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.foundation.provider.data.remote.IntegrationRemoteAccess
import stan.aac.experience.implementation.entity.fandom

class MockIntegrationRemoteAccess : IntegrationRemoteAccess {
    override fun getFandoms(query: String?): List<Fandom> {
        Thread.sleep(2_000)
        val result = listOf(
            fandom(id = "1", name = "Elder scrolls", domain = "es.fandom.com"),
            fandom(id = "2", name = "Fallout", domain = "fo.fandom.com"),
            fandom(id = "3", name = "Stalker", domain = "sr.fandom.com")
        )
        return if (query.isNullOrEmpty()) {
            result
        } else {
            result.filter {
                it.name.contains(query)
            }
        }
    }
}
