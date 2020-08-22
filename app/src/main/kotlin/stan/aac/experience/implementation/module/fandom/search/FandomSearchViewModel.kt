package stan.aac.experience.implementation.module.fandom.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.foundation.util.monad.Result
import stan.aac.experience.foundation.provider.repository.IntegrationRepository

class FandomSearchViewModel(
    private val integrationRepository: IntegrationRepository
) : ViewModel() {
    private val _fandoms: MutableLiveData<List<Fandom>> = MutableLiveData()
    val fandoms: LiveData<List<Fandom>> = _fandoms
    fun requestFandoms(query: String?) {
        viewModelScope.launch {
            when (val result = integrationRepository.getFandoms(query)) {
                is Result.Success -> {
                    _fandoms.value = result.value
                }
                is Result.Error -> {
                    // todo
                }
            }
        }
    }
}
