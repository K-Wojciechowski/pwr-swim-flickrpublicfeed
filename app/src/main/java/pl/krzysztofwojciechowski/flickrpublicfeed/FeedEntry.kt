package pl.krzysztofwojciechowski.flickrpublicfeed
import android.graphics.Bitmap
import com.squareup.picasso.Picasso

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

data class FeedEntry(val imageURL: String, val name: String, val date: Calendar, var tags: List<String>) {

    var bitmap: Bitmap? = null

    val dateString: String
        get() = formatDate(date)

    constructor(imageURL: String, name: String, date: String, tags: String) : this(
        imageURL,
        name,
        dateFromString(date),
        tags.split(",").map(String::trim).filter(String::isNotEmpty)
    )
    constructor(imageURL: String, name: String, date: String, labelerCallback: (FeedEntry)->Unit) : this(
        imageURL,
        name,
        dateFromString(date),
        listOf("…")
    ) {
        if (imageURL.isBlank()) {
            tags = listOf()
            return
        }
        val picassoImg = Picasso.get().load(imageURL).error(R.drawable.ic_no_image)
        Thread {
            bitmap = picassoImg.get()
            labelerCallback(this)
        }.start()
    }
}