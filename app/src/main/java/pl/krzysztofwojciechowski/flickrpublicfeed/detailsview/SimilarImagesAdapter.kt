package pl.krzysztofwojciechowski.flickrpublicfeed.detailsview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import pl.krzysztofwojciechowski.flickrpublicfeed.FeedEntry
import pl.krzysztofwojciechowski.flickrpublicfeed.R
import pl.krzysztofwojciechowski.flickrpublicfeed.getPicassoCreator

class SimilarImagesAdapter(private val feedEntries: MutableList<FeedEntry> = mutableListOf()) :
    RecyclerView.Adapter<SimilarImagesAdapter.SimilarImagesViewHolder>() {
    class SimilarImagesViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fpf_similar_imageview)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimilarImagesViewHolder {
        return SimilarImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.similar_grid_item, parent, false),
            parent.context
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: SimilarImagesViewHolder, position: Int) {
        val item = feedEntries[position]

        if (item.imageURL.isEmpty()) {
            holder.imageView.setImageResource(R.drawable.ic_no_image)
        } else if (item.bitmap != null) {
            holder.imageView.setImageBitmap(item.bitmap)
        } else {
            val picassoImg = getPicassoCreator(item.imageURL)
            picassoImg.into(holder.imageView)
        }
    }
    override fun getItemCount() = feedEntries.size
}
