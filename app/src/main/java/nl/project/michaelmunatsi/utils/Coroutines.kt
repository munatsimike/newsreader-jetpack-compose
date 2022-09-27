package nl.project.michaelmunatsi.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines{
    // use main thread
    fun main (work: suspend (()->Unit)) {
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }
    }

    // use io thread
    fun io (work: suspend (()->Unit)) {
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }
    }
}