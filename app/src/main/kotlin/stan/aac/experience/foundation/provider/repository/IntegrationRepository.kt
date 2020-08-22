package stan.aac.experience.foundation.provider.repository

import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.foundation.util.monad.Result

interface IntegrationRepository {
    suspend fun getFandoms(query: String?): Result<List<Fandom>>
}
