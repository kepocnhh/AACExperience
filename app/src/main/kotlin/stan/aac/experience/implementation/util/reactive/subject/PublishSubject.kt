package stan.aac.experience.implementation.util.reactive.subject

import stan.aac.experience.implementation.util.reactive.common.Subscription

fun <T : Any> publishSubject(): Subject<T> = PublishSubject()

private class PublishSubject<T : Any> : Subject<T> {
    private val subjectActions = mutableSetOf<SubjectAction<T>>()

    private inner class PublishSubjectSubscription(action: SubjectAction<T>) : Subscription {
        private var action: SubjectAction<T>? = action

        override fun unsubscribe() {
            val action = this.action
            checkNotNull(action)
            subjectActions.remove(action)
            this.action = null
        }
    }

    override fun subscribe(action: SubjectAction<T>): Subscription {
        subjectActions.add(action)
        return PublishSubjectSubscription(action)
    }

    override fun next(item: T) {
        subjectActions.forEach { it.onNext(item) }
    }
}
