package stan.aac.experience.foundation.provider.injection

import stan.aac.experience.foundation.provider.repository.IntegrationRepository

interface Injection {
    val integrationRepository: IntegrationRepository
}
