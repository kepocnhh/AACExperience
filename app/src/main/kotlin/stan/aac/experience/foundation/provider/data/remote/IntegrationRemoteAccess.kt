package stan.aac.experience.foundation.provider.data.remote

import stan.aac.experience.foundation.entity.Fandom

interface IntegrationRemoteAccess {
    fun getFandoms(query: String?): List<Fandom>
}
