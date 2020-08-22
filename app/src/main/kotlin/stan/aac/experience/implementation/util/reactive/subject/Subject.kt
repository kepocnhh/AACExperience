package stan.aac.experience.implementation.util.reactive.subject

import stan.aac.experience.implementation.util.reactive.common.Subscription

interface SubjectAction<T : Any> {
    fun onNext(item: T)
}

interface SubjectConsumer<T : Any> {
    fun subscribe(action: SubjectAction<T>): Subscription
}

interface SubjectProducer<in T : Any> {
    infix fun next(item: T)
}

interface Subject<T : Any> : SubjectConsumer<T>, SubjectProducer<T>

private class SubjectActionImpl<T : Any>(private val actionOnNext: (T) -> Unit) : SubjectAction<T> {
    override fun onNext(item: T) {
        actionOnNext(item)
    }
}

fun <T : Any> subjectAction(actionOnNext: (T) -> Unit): SubjectAction<T> {
    return SubjectActionImpl(actionOnNext)
}
