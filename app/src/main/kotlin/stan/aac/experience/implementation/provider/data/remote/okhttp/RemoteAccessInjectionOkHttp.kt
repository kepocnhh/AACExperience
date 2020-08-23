package stan.aac.experience.implementation.provider.data.remote.okhttp

import okhttp3.OkHttpClient
import stan.aac.experience.foundation.provider.converter.Converter
import stan.aac.experience.foundation.provider.data.remote.IntegrationRemoteAccess
import stan.aac.experience.foundation.provider.injection.RemoteAccessInjection
import java.util.concurrent.TimeUnit

class RemoteAccessInjectionOkHttp(
    converter: Converter
) : RemoteAccessInjection {
    override val integrationRemoteAccess: IntegrationRemoteAccess

    init {
        val client = OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()

        integrationRemoteAccess = IntegrationRemoteAccessOkHttp(client, converter.from)
    }
}
