package stan.aac.experience.implementation.provider.repository

import kotlinx.coroutines.Dispatchers
import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.foundation.provider.data.remote.IntegrationRemoteAccess
import stan.aac.experience.foundation.provider.repository.IntegrationRepository
import stan.aac.experience.foundation.util.monad.Result
import stan.aac.experience.implementation.util.monad.tryWithResult

class MockIntegrationRepository(
    private val integrationRemoteAccess: IntegrationRemoteAccess
) : IntegrationRepository {
    override suspend fun getFandoms(query: String?): Result<List<Fandom>> =
        tryWithResult(Dispatchers.IO) {
            integrationRemoteAccess.getFandoms(query)
        }
}
