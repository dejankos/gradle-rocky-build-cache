package io.github.dejankos

import org.gradle.caching.configuration.AbstractBuildCache
import java.time.Duration.ofDays

abstract class RockyBuildCacheProperties(var url: String = "", var namespace: String = "", var ttl: Long = DEFAULT_TTL) : AbstractBuildCache() {
    companion object {
        private val DEFAULT_TTL = ofDays(1).toMillis()
    }
}