package tugra.model


import kotlinx.serialization.Serializable

@Serializable
data class SpeechStatistics(
    val mostSpeeches: String? = null,
    val mostSecurity: String?,
    val leastWordy: String?
)
