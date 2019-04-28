package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: Calendar): String {
    val format = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.UK)
    return format.format(date.time)
}

fun getPicassoCreator(imageURL: String): RequestCreator = Picasso.get().load(imageURL).error(R.drawable.ic_no_image)

fun insertTagsIntoLayout(
    tagsLayout: LinearLayout,
    context: Context,
    visibleTags: List<String>
) {
    tagsLayout.removeAllViewsInLayout()
    if (visibleTags.isEmpty()) {
        val tv = buildTagTextView(context)
        tv.text = context.getText(R.string.fpf_no_tags)
        tv.setTypeface(null, Typeface.ITALIC)
        tagsLayout.addView(tv)
    }
    for (tag in visibleTags) {
        val tv = buildTagTextView(context)
        tv.text = tag
        tv.background = ContextCompat.getDrawable(context, R.drawable.fpf_tag_background)
        tagsLayout.addView(tv)
    }
}

fun buildTagTextView(context: Context): TextView {
    val tv = TextView(context)
    val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        0.0f
    )
    params.leftMargin = TAG_LEFT_MARGIN
    tv.layoutParams = params
    return tv
}
