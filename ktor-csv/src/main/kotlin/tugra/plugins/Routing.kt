package tugra.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tugra.exception.InvalidInputException
import tugra.service.SpeechDataProcessor

fun Application.configureRouting() {

    routing {
        staticResources("/", "files")

        get("/speech/evaluation") {
            val urls = call.parameters.entries().filter { (key, _) -> key.startsWith("url") }
                .flatMap { it.value }
                .toSet()

            if (urls.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "At least one 'url' parameter is required.")
                return@get
            }

            try {
                val statistics = SpeechDataProcessor().processSpeechData(urls)
                call.respond(HttpStatusCode.OK, statistics)
            } catch (e: InvalidInputException) {
                call.respond(HttpStatusCode.BadRequest, e.message.toString())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message.toString())
            }
        }
    }
}
