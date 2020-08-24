package stan.aac.experience.implementation.util.platform.android.lifecycle.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import stan.aac.experience.implementation.util.reactive.subject.BehaviorSubject
import stan.aac.experience.implementation.util.reactive.subject.SubjectConsumer
import kotlin.reflect.KClass

sealed class ViewModelCommon {
    class State(val isBusy: Boolean) : ViewModelCommon()
}

private class CompositeSubject<T : Any>(
    val signing: Int,
    val subject: BehaviorSubject<T>
)

private val isBusyMap = mutableMapOf<KClass<out ViewModel>, CompositeSubject<ViewModelCommon.State>>()

private fun ViewModel.isBusySubject(): BehaviorSubject<ViewModelCommon.State> {
    val compositeSubject = isBusyMap[this::class]
    val signing = hashCode()
    if (compositeSubject == null || compositeSubject.signing != signing) {
        val result = BehaviorSubject<ViewModelCommon.State>()
        isBusyMap[this::class] = CompositeSubject(signing = signing, subject = result)
        return result
    }
    return compositeSubject.subject
}

fun ViewModel.isBusyConsumer(): SubjectConsumer<ViewModelCommon.State> {
    return isBusySubject()
}

private val jobs = mutableMapOf<Any, Job>()

private object CancellationExceptionInternal : CancellationException()

private fun ViewModel.onStartJob() {
    val subject = isBusySubject()
    val oldValue = subject.getValueOrNull()
    if (oldValue == null || !oldValue.isBusy) {
        subject next ViewModelCommon.State(isBusy = true)
    }
}

private fun ViewModel.onFinishJob() {
    val subject = isBusySubject()
    val oldValue = subject.getValueOrNull()
    if (oldValue != null && oldValue.isBusy) {
        subject next ViewModelCommon.State(isBusy = false)
    }
}

fun ViewModel.launch(
    key: Any = this::class.java.name + "." + Thread.currentThread().stackTrace[3].methodName,
    block: suspend CoroutineScope.() -> Unit
) {
    val oldJob = jobs[key]
    val newJob = if (oldJob == null || !oldJob.isActive) {
        onStartJob()
        viewModelScope.launch(block = block)
    } else {
        oldJob.cancel(CancellationExceptionInternal)
        viewModelScope.launch {
            oldJob.join()
            onStartJob()
            block()
        }
    }
    newJob.invokeOnCompletion {
        if (it !== CancellationExceptionInternal) {
            onFinishJob()
        }
    }
    jobs[key] = newJob
}
