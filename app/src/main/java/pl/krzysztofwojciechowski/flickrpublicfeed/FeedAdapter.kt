package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedAdapter(private val showImageScreen: (FeedEntry, List<FeedEntry>) -> Unit, private val feedEntries: MutableList<FeedEntry> = mutableListOf()) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    class FeedViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fpf_card_imageview)
        val nameView: TextView = itemView.findViewById(R.id.fpf_card_name)
        val dateView: TextView = itemView.findViewById(R.id.fpf_card_date)
        val tagsLayout: LinearLayout = itemView.findViewById(R.id.fpf_card_tags_layout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedViewHolder {
        return FeedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.feed_list_item, parent, false),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = feedEntries[position]
        holder.itemView.setOnClickListener({ _ -> showImageScreen(item, findImagesWithMatchingTags(item)) })

        if (item.imageURL.isEmpty()) {
            holder.imageView.setImageResource(R.drawable.ic_no_image)
        } else if (item.bitmap != null) {
            holder.imageView.setImageBitmap(item.bitmap)
        } else {
            getPicassoImageOrPlaceholder(getPicassoCreator(item.imageURL), holder.imageView)
        }

        if (item.name.isEmpty()) {
            holder.nameView.visibility = View.GONE
        } else {
            holder.nameView.visibility = View.VISIBLE
            holder.nameView.text = item.name
        }
        holder.dateView.text = item.dateString
        val visibleTags = item.tags.take(MAX_TAG_COUNT)
        insertTagsIntoLayout(holder.tagsLayout, holder.context, visibleTags)
    }


    fun addItem(entry: FeedEntry) {
        feedEntries.add(0, entry)
        notifyItemInserted(0)
    }

    fun addItems(entries: List<FeedEntry>) {
        feedEntries.addAll(0, entries)
        notifyItemRangeInserted(0, entries.size)
    }

    fun updateItem(entry: FeedEntry) {
        val position = feedEntries.indexOf(entry)
        if (position == -1) {
            Log.wtf("FeedAdapter", "Update item received but item not found, updating all")
            notifyItemRangeChanged(0, feedEntries.size)
        } else {
            notifyItemChanged(position)
        }
    }

    fun findImagesWithMatchingTags(entry: FeedEntry): List<FeedEntry> =
        feedEntries.mapNotNull { e ->
            if (e == entry) null else {
                val union = e.tags.union(entry.tags)
                if (union.isEmpty()) null else Pair(e, union)
            }
        }.sortedBy { (_, union) -> union.size }.
            map { (e, _) -> e }.take(SIMILAR_IMAGE_COUNT)

    fun removeItem(adjPosition: Int) {
        feedEntries.removeAt(adjPosition)
        notifyItemRemoved(adjPosition)
    }

    override fun getItemCount() = feedEntries.size
}
