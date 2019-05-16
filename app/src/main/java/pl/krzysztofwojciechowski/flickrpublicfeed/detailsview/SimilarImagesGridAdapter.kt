package pl.krzysztofwojciechowski.flickrpublicfeed.detailsview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import pl.krzysztofwojciechowski.flickrpublicfeed.FeedEntry
import pl.krzysztofwojciechowski.flickrpublicfeed.getPicassoCreator
import pl.krzysztofwojciechowski.flickrpublicfeed.getPicassoImageOrPlaceholder

class SimilarImagesGridAdapter(private val ctx: Context, private val feedEntries: List<FeedEntry>) : BaseAdapter() {

    override fun getCount(): Int {
        return feedEntries.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val iv: ImageView
        if (convertView == null) {
            iv = ImageView(ctx)
            iv.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            iv = convertView as ImageView
        }
        getPicassoImageOrPlaceholder(
            getPicassoCreator(feedEntries[position].imageURL).centerCrop().resize(400, 400),
            iv)
        return iv
    }
}