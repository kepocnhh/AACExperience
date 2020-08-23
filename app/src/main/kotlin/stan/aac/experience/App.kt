package stan.aac.experience

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import stan.aac.experience.foundation.provider.converter.Converter
import stan.aac.experience.foundation.provider.injection.Injection
import stan.aac.experience.foundation.provider.injection.RemoteAccessInjection
import stan.aac.experience.foundation.provider.repository.IntegrationRepository
import stan.aac.experience.implementation.provider.converter.jackson.JacksonConverter
import stan.aac.experience.implementation.provider.data.remote.okhttp.RemoteAccessInjectionOkHttp
import stan.aac.experience.implementation.provider.platform.android.lifecycle.ViewModelFactory
import stan.aac.experience.implementation.provider.repository.IntegrationRepositoryIO

private class AppInjection(override val integrationRepository: IntegrationRepository) : Injection

class App : Application() {
    companion object {
        private lateinit var appInjection: Injection
        val injection get() = appInjection
        private lateinit var appViewModelFactory: ViewModelProvider.Factory
        val viewModelFactory get() = appViewModelFactory
    }

    override fun onCreate() {
        super.onCreate()
        val converter: Converter = JacksonConverter
        val remoteAccessInjection: RemoteAccessInjection = RemoteAccessInjectionOkHttp(converter = converter)
        appInjection = AppInjection(
            integrationRepository = IntegrationRepositoryIO(
                integrationRemoteAccess = remoteAccessInjection.integrationRemoteAccess
            )
        )
        appViewModelFactory = ViewModelFactory(injection)
    }
}
