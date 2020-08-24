package stan.aac.experience.implementation.module.fandom.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.foundation.util.monad.Result
import stan.aac.experience.foundation.provider.repository.IntegrationRepository
import stan.aac.experience.implementation.util.reactive.subject.Subject
import stan.aac.experience.implementation.util.reactive.subject.SubjectConsumer
import stan.aac.experience.implementation.util.reactive.subject.publishSubject
import stan.aac.experience.implementation.util.platform.android.lifecycle.coroutines.launch

class FandomSearchViewModel(
    private val integrationRepository: IntegrationRepository
) : ViewModel() {
    class Broadcast {
        init {
            error("No instance!")
        }

        sealed class Out {
            class Error(cause: Throwable) : Out()
        }
    }

    private val subjectOut: Subject<Broadcast.Out> = publishSubject()
    val subjectOutConsumer: SubjectConsumer<Broadcast.Out> = subjectOut

    private val _fandoms: MutableLiveData<List<Fandom>> = MutableLiveData()

    val fandoms: LiveData<List<Fandom>> = _fandoms

    fun requestFandoms(query: String?) {
        launch {
            delay(2_000)
            when (val result = integrationRepository.getFandoms(query)) {
                is Result.Success -> {
                    _fandoms.value = result.value
                }
                is Result.Error -> {
                    subjectOut next Broadcast.Out.Error(result.error)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared() // todo behaviour
    }
}
