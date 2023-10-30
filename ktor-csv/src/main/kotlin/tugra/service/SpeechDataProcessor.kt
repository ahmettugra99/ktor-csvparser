package tugra.service

import tugra.exception.InvalidInputException
import tugra.model.SpeechData
import tugra.model.SpeechStatistics
import tugra.service.checker.isCsvFileUrlsValid

class SpeechDataProcessor {


    fun processSpeechData(csvUrls: Set<String>): SpeechStatistics {
        if (isCsvFileUrlsValid(csvUrls)) {
            val speeches: List<SpeechData> = SpeechDataDownloader.getCsvFilesFromUrls(csvUrls)
            val mostSpeechesIn2013 = speeches.politicianWithMostSpeeches(2013)
            val mostSpeechesOnHomelandSecurity = speeches.politicianWithMostSpeechesOnTopic("homeland security")
            val fewestWordsOverall = speeches.politicianWithFewestWords()

            return SpeechStatistics(mostSpeechesIn2013, mostSpeechesOnHomelandSecurity, fewestWordsOverall)
        } else {
            throw InvalidInputException("The URLs are not reachable or not a CSV file")
        }
    }

    private fun List<SpeechData>.politicianWithMostSpeeches(year: Int): String? {
        return this.filter { it.date.year == year }
            .groupingBy { it.speaker }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }

    private fun List<SpeechData>.politicianWithMostSpeechesOnTopic(topic: String): String? {
        return this.filter { it.topic.equals(topic, ignoreCase = true) }
            .groupingBy { it.speaker }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }

    private fun List<SpeechData>.politicianWithFewestWords(): String? {
        return this.groupingBy { it.speaker }
            .fold(0L) { accumulator, element -> accumulator + element.words }
            .minByOrNull { it.value }
            ?.key
    }


}