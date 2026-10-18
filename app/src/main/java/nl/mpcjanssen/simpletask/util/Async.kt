package nl.mpcjanssen.simpletask.util

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

// Minimal replacement for the doAsync/uiThread helpers from anko-commons,
// which is no longer available (jcenter shut down). Same semantics: run the
// task on a shared background pool, log and swallow exceptions, and only run
// uiThread blocks while the receiver (e.g. an Activity) is still reachable.

class AsyncContext<T>(val weakRef: WeakReference<T>)

private object BackgroundExecutor {
    val executor: ExecutorService =
            Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())
}

private val mainHandler = Handler(Looper.getMainLooper())

fun <T> T.doAsync(task: AsyncContext<T>.() -> Unit): Future<Unit> {
    val context = AsyncContext(WeakReference(this))
    return BackgroundExecutor.executor.submit<Unit> {
        try {
            context.task()
        } catch (t: Throwable) {
            Log.e("doAsync", "Background task failed", t)
        }
    }
}

fun <T> AsyncContext<T>.uiThread(f: (T) -> Unit): Boolean {
    val ref = weakRef.get() ?: return true
    if (ref is Activity && ref.isFinishing) return true
    if (Looper.getMainLooper() === Looper.myLooper()) {
        f(ref)
    } else {
        mainHandler.post { f(ref) }
    }
    return true
}
