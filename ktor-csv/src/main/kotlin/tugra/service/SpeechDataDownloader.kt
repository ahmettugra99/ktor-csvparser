package tugra.service

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import tugra.exception.InvalidInputException
import tugra.model.SpeechData
import java.io.InputStreamReader
import java.net.URL
import java.time.LocalDate

class SpeechDataDownloader {
    companion object {
        fun getCsvFilesFromUrls(urls: Set<String>): List<SpeechData> {
            val csvData: MutableList<SpeechData> = mutableListOf()

            for (url in urls) {
                csvData.addAll(parseCsvFromUrl(url))
            }
            return csvData
        }

        private fun parseCsvFromUrl(url: String): List<SpeechData> {
            val speechData: MutableList<SpeechData> = mutableListOf()

            try {
                val csvParser = CSVParserBuilder().withSeparator(';').build()
                val csvReader = CSVReaderBuilder(InputStreamReader(URL(url).openStream()))
                    .withCSVParser(csvParser)
                    .withSkipLines(1)
                    .build()

                var record: Array<String>?
                while (csvReader.readNext().also { record = it } != null) {
                    if (record!!.size == 4) {
                        val speaker = record!![0].trim()
                        val topic = record!![1].trim()
                        val date = LocalDate.parse(record!![2].trim())
                        val words = record!![3].trim().toLong()
                        val speech = SpeechData(speaker, topic, date, words)
                        speechData.add(speech)
                    } else {
                        throw InvalidInputException("Invalid CSV format. Each record should have 4 fields.")
                    }
                }
                csvReader.close()
            } catch (e: Exception) {
                throw InvalidInputException("CSV file cannot be parsed. File format is not valid or file is corrupted.")
            }

            return speechData
        }
    }
}