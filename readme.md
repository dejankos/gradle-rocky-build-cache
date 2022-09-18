## Remote build cache for Rocky storage

[![Build Gradle Rocky build cache](https://github.com/dejankos/gradle-rocky-build-cache/actions/workflows/build-gradle.yml/badge.svg)](https://github.com/dejankos/gradle-rocky-build-cache/actions/workflows/build-gradle.yml)

Remote build cache for [Rocky storage](https://github.com/dejankos/Rocky)

### Usage

```kotlin
apply(plugin = "io.github.dejankos.gradle-rocky-build-cache")

buildCache {
    local {
        isEnabled = false
    }

    remote(io.github.dejankos.RockyBuildCacheProperties::class) {
        url = "http://localhost:8080"
        namespace = rootProject.name
        ttl = 10_000
    }
}
```
