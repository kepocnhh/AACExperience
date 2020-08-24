package stan.aac.experience.implementation.util.reactive.subject

import stan.aac.experience.implementation.util.reactive.common.Subscription

class BehaviorSubject<T : Any> : Subject<T> {
    private val subjectActions = mutableSetOf<SubjectAction<T>>()
    private var value: T? = null

    fun getValueOrNull(): T? {
        return value
    }

    private inner class BehaviorSubjectSubscription(action: SubjectAction<T>) : Subscription {
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
        value?.also(action::onNext)
        return BehaviorSubjectSubscription(action)
    }

    override fun next(item: T) {
        value = item
        subjectActions.forEach { it.onNext(item) }
    }
}
