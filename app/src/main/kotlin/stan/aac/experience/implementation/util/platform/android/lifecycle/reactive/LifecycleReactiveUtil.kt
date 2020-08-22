package stan.aac.experience.implementation.util.platform.android.lifecycle.reactive

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import stan.aac.experience.implementation.util.reactive.common.Subscription
import stan.aac.experience.implementation.util.reactive.subject.SubjectAction
import stan.aac.experience.implementation.util.reactive.subject.SubjectConsumer
import stan.aac.experience.implementation.util.reactive.subject.subjectAction

private class SubscriptionLifecycleObserver(
    lifecycle: Lifecycle,
    subscriptionsSupplier: () -> Iterable<Subscription>
) : LifecycleObserver {
    private var lifecycle: Lifecycle? = lifecycle
    private var subscriptionsSupplier: (() -> Iterable<Subscription>)? = subscriptionsSupplier
    private var subscriptions: Iterable<Subscription>? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        val subscriptionsSupplier = this.subscriptionsSupplier
        checkNotNull(subscriptionsSupplier)
        subscriptions = subscriptionsSupplier()
        this.subscriptionsSupplier = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        val subscriptions = this.subscriptions
        checkNotNull(subscriptions)
        subscriptions.forEach { it.unsubscribe() }
        this.subscriptions = null
        val lifecycle = this.lifecycle
        checkNotNull(lifecycle)
        lifecycle.removeObserver(this)
        this.lifecycle = null
    }
}

fun <T : Any> Lifecycle.subscribe(
    subjectConsumer: SubjectConsumer<T>,
    subjectAction: SubjectAction<T>
) {
    addObserver(
        SubscriptionLifecycleObserver(this) {
            listOf(subjectConsumer.subscribe(subjectAction))
        }
    )
}

interface SubjectPair<T : Any> {
    val consumer: SubjectConsumer<T>
    val action: SubjectAction<T>
}

private class SubjectPairImpl<T : Any>(
    override val consumer: SubjectConsumer<T>,
    override val action: SubjectAction<T>
) : SubjectPair<T>

infix fun <T : Any> SubjectConsumer<T>.to(subjectAction: SubjectAction<T>): SubjectPair<T> {
    return SubjectPairImpl(this, subjectAction)
}

infix fun <T : Any> SubjectConsumer<T>.toAction(actionOnNext: (T) -> Unit): SubjectPair<T> {
    return to(subjectAction(actionOnNext))
}

fun <T : Any> Lifecycle.subscribe(
    subjectPair: SubjectPair<T>
) {
    subscribe(subjectPair.consumer, subjectPair.action)
}

fun Lifecycle.subscribeAll(
    subjectPairFirst: SubjectPair<*>,
    subjectPairSecond: SubjectPair<*>,
    vararg subjectPairs: SubjectPair<*>
) {
    addObserver(
        SubscriptionLifecycleObserver(this) {
            subjectPairFirst as SubjectPair<Any>
            subjectPairSecond as SubjectPair<Any>
            val result = mutableListOf(
                subjectPairFirst.consumer.subscribe(subjectPairFirst.action),
                subjectPairSecond.consumer.subscribe(subjectPairSecond.action)
            )
            result.addAll(
                subjectPairs.map {
                    it as SubjectPair<Any>
                    it.consumer.subscribe(it.action)
                }
            )
            result
        }
    )
}
