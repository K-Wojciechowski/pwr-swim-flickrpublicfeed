package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FeedAdapter(private val feedEntries: MutableList<FeedEntry> = mutableListOf()) :
    RecyclerView.Adapter<FeedAdapter.HistoryViewHolder>() {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class HistoryViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fpf_card_imageview)
        val nameView: TextView = itemView.findViewById(R.id.fpf_card_name)
        val dateView: TextView = itemView.findViewById(R.id.fpf_card_date)
        val tagsLayout: LinearLayout = itemView.findViewById(R.id.fpf_card_tags_layout)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedAdapter.HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.feed_list_item, parent, false),
            parent.context
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        // newest at top
        val item = feedEntries[feedEntries.size - 1 - position]

        Picasso.get().load(item.imageURL).into(holder.imageView)
        holder.nameView.text = item.name
        holder.dateView.text = item.dateString
        val visibleTags = item.tags.take(MAX_TAG_COUNT)
        if (visibleTags.isNotEmpty()) {
            holder.tagsLayout.removeAllViewsInLayout()
        }
        for (tag in visibleTags) {
            val tv = TextView(holder.context)
            tv.text = tag
            tv.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.0f
            )
            tv.setPadding(0, 0, 2, 0)
            tv.background = ContextCompat.getDrawable(holder.context, R.drawable.fpf_tag_background)
            holder.tagsLayout.addView(tv)
        }
    }

    fun addItem(entry: FeedEntry) {
        feedEntries.add(entry)
        notifyItemInserted(0)
    }

    fun removeItem(position: Int) {
        feedEntries.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount() = feedEntries.size
}
