package pl.krzysztofwojciechowski.flickrpublicfeed

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: Calendar): String {
    val format = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.UK)
    return format.format(date.time)
}
