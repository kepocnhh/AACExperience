package stan.aac.experience.foundation.provider.injection

import stan.aac.experience.foundation.provider.data.remote.IntegrationRemoteAccess

interface RemoteAccessInjection {
    val integrationRemoteAccess: IntegrationRemoteAccess
}
