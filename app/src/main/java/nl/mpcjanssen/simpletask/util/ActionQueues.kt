package nl.mpcjanssen.simpletask.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executors


open class ActionQueue(val qName: String) : Thread() {

    // One worker thread, so queued actions (save, reload, backup) run in order
    private val executor = Executors.newSingleThreadExecutor()

    fun add(description: String, r: () -> Unit) {
        Log.i(qName, "-> $description")
        executor.execute {
            Log.i(qName, "<- $description")
            try {
                r.invoke()
            } catch (t: Throwable) {
                Log.e(qName, "Action failed: $description", t)
            }
        }
    }
}

object FileStoreActionQueue : ActionQueue("FSQ")



