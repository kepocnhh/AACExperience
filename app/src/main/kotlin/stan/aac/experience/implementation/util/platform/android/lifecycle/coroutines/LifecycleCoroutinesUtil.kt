package stan.aac.experience.implementation.util.platform.android.lifecycle.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

private val isBusyMap = mutableMapOf<KClass<out ViewModel>, MutableLiveData<Boolean>>()

private fun ViewModel.isBusyMutable(): MutableLiveData<Boolean> {
    return isBusyMap[this::class]
        ?: MutableLiveData<Boolean>().also {
            isBusyMap[this::class] = it
        }
}

fun ViewModel.isBusy(): LiveData<Boolean> {
    return isBusyMutable()
}

private val jobs = mutableMapOf<Any, Job>()

private object CancellationExceptionInternal : CancellationException()

private fun ViewModel.onStartJob() {
    val isBusy = isBusyMutable()
    if (isBusy.value != true) {
        isBusy.value = true
    }
}

private fun ViewModel.onFinishJob() {
    val isBusy = isBusyMutable()
    if (isBusy.value == true) {
        isBusy.value = false
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
