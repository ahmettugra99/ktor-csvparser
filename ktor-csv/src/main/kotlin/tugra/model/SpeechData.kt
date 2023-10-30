package tugra.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import tugra.util.LocalDateSerializer
import java.time.LocalDate


@Serializable
data class SpeechData(
    val speaker: String,
    val topic: String,
    @Serializable(with = LocalDateSerializer::class)
    @Contextual
    val date: LocalDate,
    val words: Long
)
