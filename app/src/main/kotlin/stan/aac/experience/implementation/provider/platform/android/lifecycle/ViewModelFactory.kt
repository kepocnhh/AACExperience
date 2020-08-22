package stan.aac.experience.implementation.provider.platform.android.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import stan.aac.experience.foundation.provider.injection.Injection
import stan.aac.experience.implementation.module.fandom.search.FandomSearchViewModel

class ViewModelFactory(
    private val injection: Injection
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FandomSearchViewModel::class.java)) {
            return FandomSearchViewModel(injection.integrationRepository) as T
        }
        error("ViewModel by ${modelClass.name} not supported!")
    }
}
