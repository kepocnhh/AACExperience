package stan.aac.experience.implementation.util.monad

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import stan.aac.experience.foundation.util.monad.Result
import kotlin.coroutines.CoroutineContext

fun <T : Any> tryWithResult(block: () -> T): Result<T> {
    val result = try {
        block()
    } catch (error: Throwable) {
        return Result.Error(error)
    }
    return Result.Success(result)
}

suspend fun <T : Any> tryWithResult(
    context: CoroutineContext,
    block: () -> T
): Result<T> = withContext(context) {
    tryWithResult(block)
}
