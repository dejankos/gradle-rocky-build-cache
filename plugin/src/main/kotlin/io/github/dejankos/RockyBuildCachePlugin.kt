package io.github.dejankos

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class RockyBuildCachePlugin : Plugin<Settings> {

    override fun apply(settings: Settings) {
        settings.buildCache {
            registerBuildCacheService(RockyBuildCacheProperties::class.java, RockyBuildCacheServiceFactory::class.java)
            remote(RockyBuildCacheProperties::class.java) {
                isPush = true
            }
        }
    }
}