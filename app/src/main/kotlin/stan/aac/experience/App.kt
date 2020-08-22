package stan.aac.experience

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import stan.aac.experience.foundation.provider.injection.Injection
import stan.aac.experience.foundation.provider.repository.IntegrationRepository
import stan.aac.experience.implementation.provider.data.remote.MockIntegrationRemoteAccess
import stan.aac.experience.implementation.provider.platform.android.lifecycle.ViewModelFactory
import stan.aac.experience.implementation.provider.repository.MockIntegrationRepository

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
        appInjection = AppInjection(
            integrationRepository = MockIntegrationRepository(
                integrationRemoteAccess = MockIntegrationRemoteAccess()
            )
        )
        appViewModelFactory = ViewModelFactory(injection)
    }
}
