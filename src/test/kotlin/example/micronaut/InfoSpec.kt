package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

object InfoSpec: Spek({
    describe("info endpoint") {
        val embeddedServer : EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
        val client : HttpClient  = HttpClient.create(embeddedServer.url)
        it("test git commit info appears in JSON") {
            val request: HttpRequest<Any> = HttpRequest.GET("/info")
            val rsp = client.toBlocking().exchange(request, Map::class.java)

            assertEquals(rsp.status(), HttpStatus.OK)

            @Suppress("UNCHECKED_CAST")
            val json : Map<String, Any> = rsp.body() as Map<String, Any>

            assertNotNull(json["git"])
            @Suppress("UNCHECKED_CAST")
            val mapCommit = (json["git"] as Map<String, Any>)["commit"] as Map<String, Any>
            assertNotNull(mapCommit)
            assertNotNull(mapCommit["message"])
            assertNotNull(mapCommit["time"])
            assertNotNull(mapCommit["id"])
            assertNotNull(mapCommit["user"])
            @Suppress("UNCHECKED_CAST")
            assertNotNull((json["git"] as Map<String, Any>)["branch"])

        }
        afterGroup {
            client.close()
            embeddedServer.close()
        }

    }
})