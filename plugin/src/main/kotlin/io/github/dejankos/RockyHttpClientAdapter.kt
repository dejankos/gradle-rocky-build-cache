package io.github.dejankos

import org.gradle.caching.BuildCacheException
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpClient.Version.HTTP_2
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers.noBody
import java.net.http.HttpRequest.BodyPublishers.ofByteArray
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers.discarding
import java.net.http.HttpResponse.BodyHandlers.ofInputStream
import java.time.Duration.ofSeconds
import java.util.concurrent.Executors.newCachedThreadPool

class RockyHttpClientAdapter(url: String, namespace: String, private val ttl: Long) {

    private val baseURIStr = "$url/$namespace"
    private val baseURI = baseURIStr.toURI()

    companion object {
        private val NAMED_THREAD_FACTORY = NamedThreadFactory("rocky-http-client-adapter")
    }

    private val httpClient = HttpClient.newBuilder()
        .version(HTTP_2)
        .connectTimeout(ofSeconds(5))
        .executor(newCachedThreadPool(NAMED_THREAD_FACTORY))
        .build()

    fun containsNamespace() =
        httpClient.send(
            HttpRequest.newBuilder()
                .uri(baseURI)
                .GET()
                .build(), discarding()
        )
            .throwOnError()
            .is200()

    fun createNamespace() {
        httpClient.send(
            HttpRequest.newBuilder()
                .uri(baseURI)
                .POST(noBody())
                .build(), discarding()
        ).throwOnError()
    }

    fun read(key: String): InputStream = httpClient.send(
        HttpRequest.newBuilder()
            .uri("$baseURIStr/$key".toURI())
            .GET()
            .build(), ofInputStream()
    ).throwOnError()
        .body()

    fun write(key: String, bytes: ByteArray) = httpClient.send(
        HttpRequest.newBuilder()
            .uri("$baseURIStr/$key".toURI())
            .POST(ofByteArray(bytes))
            .header("ttl", ttl.toString())
            .build(), discarding()
    ).throwOnError()

    private fun String.toURI() = URI.create(this)

    private fun <T> HttpResponse<T>.is200() = this.statusCode() == 200

    private fun <T> HttpResponse<T>.throwOnError() = if (!isSuccess(this.statusCode()))
        throw BuildCacheException("Remote call ${this.uri()} failed with status code = ${this.statusCode()}")
    else
        this

    private fun isSuccess(statusCode: Int) = statusCode in 200..299
}