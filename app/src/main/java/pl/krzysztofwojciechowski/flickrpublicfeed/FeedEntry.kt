package pl.krzysztofwojciechowski.flickrpublicfeed
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

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

data class FeedEntry(val imageURL: String, val name: String, val date: Calendar, var tags: List<String>): Parcelable {

    var bitmap: Bitmap? = null

    val dateString: String
        get() = formatDate(date)

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        dateFromString(parcel.readString()!!),
        parcel.createStringArrayList()!!
    )

    constructor(imageURL: String, name: String, date: String, tags: String) : this(
        imageURL,
        name,
        dateFromString(date),
        tags.split(",").map(String::trim).filter(String::isNotEmpty)
    )
    constructor(
        imageURL: String,
        name: String,
        date: String,
        labelerCallback: (FeedEntry) -> Unit
    ) : this(
        imageURL,
        name,
        dateFromString(date),
        listOf("…")
    ) {
        if (imageURL.isBlank()) {
            tags = listOf()
            return
        }
        val picassoImg = getPicassoCreator(imageURL)

        Thread {
            bitmap = getPicassoImageOrNull(picassoImg)
            labelerCallback(this)
        }.start()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageURL)
        parcel.writeString(name)
        parcel.writeString(dateString)
        parcel.writeStringList(tags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeedEntry> {
        override fun createFromParcel(parcel: Parcel): FeedEntry {
            return FeedEntry(parcel)
        }

        override fun newArray(size: Int): Array<FeedEntry?> {
            return arrayOfNulls(size)
        }
    }
}