package tugra.service.checker

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


fun isCsvFileUrlsValid(csvFileUrls: Set<String>): Boolean {
    return csvFileUrls.all { url ->
        url.isNotBlank() &&
                isUrlAccessible(url) &&
                isCSVFile(url)
    }
}

fun isCSVFile(url: String): Boolean {
    return url.endsWith(".csv", ignoreCase = true)
}

fun isUrlAccessible(url: String): Boolean {
    return try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        connection.responseCode == HttpURLConnection.HTTP_OK
    } catch (e: IOException) {
        false
    }
}

