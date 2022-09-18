package io.github.dejankos

import org.gradle.caching.BuildCacheEntryReader
import org.gradle.caching.BuildCacheEntryWriter
import org.gradle.caching.BuildCacheKey
import org.gradle.caching.BuildCacheService
import java.io.ByteArrayOutputStream
import java.io.InputStream

class RockyBuildCacheService(url: String, namespace: String, ttl: Long) : BuildCacheService {
    private val log = logger()
    private val clientAdapter = RockyHttpClientAdapter(url, namespace, ttl)

    init {
        log.debug("Initializing project cache namespace $namespace.")
        if (clientAdapter.containsNamespace()) {
            log.debug("Project cache namespace $namespace already exists.")
        } else {
            clientAdapter.createNamespace()
            log.debug("Project cache namespace $namespace initialized.")
        }
    }

    override fun close() {}

    override fun load(key: BuildCacheKey, reader: BuildCacheEntryReader): Boolean {
        val hash = key.hashCode.also {
            log.debug("Loading build cache key $it")
        }

        val inStream = clientAdapter.read(hash)
        if (inStream.isPresent()) {
            reader.readFrom(inStream)
            return true
        }

        return false
    }

    override fun store(key: BuildCacheKey, writer: BuildCacheEntryWriter) {
        val hash = key.hashCode.also {
            log.debug("Storing build cache key $it")
        }

        clientAdapter.write(hash, writer.toByteArray())
    }

    private fun BuildCacheEntryWriter.toByteArray() = ByteArrayOutputStream().let {
        this.writeTo(it)
        it
    }.toByteArray()

    private fun InputStream.isPresent(): Boolean = this.let { stream ->
        if (stream.markSupported()) {
            stream.mark(1)
            val byte = stream.read(ByteArray(1)).also { stream.reset() }
            return byte != -1
        }

        return stream.available() > 0
    }
}