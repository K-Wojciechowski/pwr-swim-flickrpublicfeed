package pl.krzysztofwojciechowski.flickrpublicfeed

import android.net.Uri
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: Calendar): String {
    val format = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.UK)
    return format.format(date.time)
}

fun getPicassoCreator(imageURL: String): RequestCreator = Picasso.get().load(imageURL).error(R.drawable.ic_no_image)