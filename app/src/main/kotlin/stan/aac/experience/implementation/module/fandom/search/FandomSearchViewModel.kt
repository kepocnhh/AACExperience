package stan.aac.experience.implementation.module.fandom.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.foundation.util.monad.Result
import stan.aac.experience.foundation.provider.repository.IntegrationRepository
import stan.aac.experience.implementation.util.reactive.subject.Subject
import stan.aac.experience.implementation.util.reactive.subject.SubjectConsumer
import stan.aac.experience.implementation.util.reactive.subject.publishSubject
import stan.aac.experience.implementation.util.platform.android.lifecycle.coroutines.launch
import stan.aac.experience.implementation.util.reactive.subject.BehaviorSubject

class FandomSearchViewModel(
    private val integrationRepository: IntegrationRepository
) : ViewModel() {
    sealed class Broadcast {
        class Error(cause: Throwable) : Broadcast()
    }

    sealed class Data {
        class Fandoms(val list: List<Fandom>) : Data()
    }

    private val subjectBroadcast: Subject<Broadcast> = publishSubject()
    val broadcastConsumer: SubjectConsumer<Broadcast> = subjectBroadcast

    private val subjectData: Subject<Data> = BehaviorSubject()
    val dataConsumer: SubjectConsumer<Data> = subjectData

    fun requestFandoms(query: String?) {
        launch {
            delay(2_000)
            when (val result = integrationRepository.getFandoms(query)) {
                is Result.Success -> {
                    subjectData next Data.Fandoms(result.value)
                }
                is Result.Error -> {
                    subjectBroadcast next Broadcast.Error(result.error)
                }
            }
        }
    }
}
