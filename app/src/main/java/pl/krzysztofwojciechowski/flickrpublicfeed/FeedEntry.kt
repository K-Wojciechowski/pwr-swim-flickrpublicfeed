package pl.krzysztofwojciechowski.flickrpublicfeed

import java.util.*

fun dateFromString(date: String): Calendar {
    val parts = date.split("-")
    val year = parts[0].toInt()
    val month = parts[1].toInt()
    val day = parts[2].toInt()
    val cal = Calendar.getInstance()
    cal.set(year, month, day)
    return cal
    // class Date is deprecated, Calendar is good enough and built-in
}

data class FeedEntry(val imageURL: String, val name: String, val date: Calendar, val tags: List<String>) {
    val dateString: String
        get() = formatDate(date)

    constructor(imageURL: String, name: String, date: String, tags: String) : this(
        imageURL,
        name,
        dateFromString(date),
        tags.split(",").map(String::trim).filter(String::isNotEmpty)
    )
}