package stan.aac.experience.foundation.util.monad

sealed class Result<out T : Any> {
    class Success<out T : Any>(val value: T) : Result<T>()
    class Error(val error: Throwable) : Result<Nothing>()
}
