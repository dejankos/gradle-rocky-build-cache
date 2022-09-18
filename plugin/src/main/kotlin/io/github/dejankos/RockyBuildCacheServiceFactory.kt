package io.github.dejankos

import org.gradle.caching.BuildCacheService
import org.gradle.caching.BuildCacheServiceFactory

class RockyBuildCacheServiceFactory : BuildCacheServiceFactory<RockyBuildCacheProperties> {

    override fun createBuildCacheService(configuration: RockyBuildCacheProperties, describer: BuildCacheServiceFactory.Describer): BuildCacheService {
        describer.type("Rocky Build Cache")
            .config("url", configuration.url)
            .config("namespace", configuration.namespace)
            .config("ttl", configuration.ttl.toString())

        return RockyBuildCacheService(configuration.url, configuration.namespace, configuration.ttl)
    }
}