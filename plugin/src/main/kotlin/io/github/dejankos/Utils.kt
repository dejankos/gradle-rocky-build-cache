package io.github.dejankos

import org.gradle.api.logging.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class NamedThreadFactory(baseName: String) : ThreadFactory {
    private val threadsNum = AtomicInteger()
    private val namePattern = "$baseName-%d"

    override fun newThread(runnable: Runnable) =
         Thread(runnable, String.format(namePattern, threadsNum.addAndGet(1)))
}


fun <T : Any> T.logger() : Logger = LoggerFactory.getLogger(javaClass) as Logger

