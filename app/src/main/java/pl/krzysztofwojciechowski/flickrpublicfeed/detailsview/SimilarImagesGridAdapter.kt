package pl.krzysztofwojciechowski.flickrpublicfeed.detailsview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import pl.krzysztofwojciechowski.flickrpublicfeed.FeedEntry
import pl.krzysztofwojciechowski.flickrpublicfeed.getPicassoCreator

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
//            iv.layoutParams = ViewGroup.LayoutParams(300, 300)
//            iv.setPadding(8, 8, 8, 8)
        } else {
            iv = convertView as ImageView
        }
//        getPicassoCreator(feedEntries[position].imageURL).into(iv)
        getPicassoCreator(feedEntries[position].imageURL).centerCrop().resize(400, 400).into(iv)
        return iv
    }
}