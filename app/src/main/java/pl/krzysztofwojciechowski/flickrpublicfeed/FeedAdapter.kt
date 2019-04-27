package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Context
import android.graphics.Typeface
import android.util.Log
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
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class FeedViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fpf_card_imageview)
        val nameView: TextView = itemView.findViewById(R.id.fpf_card_name)
        val dateView: TextView = itemView.findViewById(R.id.fpf_card_date)
        val tagsLayout: LinearLayout = itemView.findViewById(R.id.fpf_card_tags_layout)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedViewHolder {
        return FeedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.feed_list_item, parent, false),
            parent.context
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = feedEntries[position]

        if (item.imageURL.isEmpty()) {
            holder.imageView.setImageResource(R.drawable.ic_no_image)
        } else if (item.bitmap != null) {
            holder.imageView.setImageBitmap(item.bitmap)
        } else {
            val picassoImg = Picasso.get().load(item.imageURL).error(R.drawable.ic_no_image)
            picassoImg.into(holder.imageView)
        }

        if (item.name.isEmpty()) {
            holder.nameView.visibility = View.GONE
        } else {
            holder.nameView.visibility = View.VISIBLE
            holder.nameView.text = item.name
        }
        holder.dateView.text = item.dateString
        val visibleTags = item.tags.take(MAX_TAG_COUNT)
        holder.tagsLayout.removeAllViewsInLayout()
        if (visibleTags.isEmpty()) {
            val tv = buildTagTextView(holder.context)
            tv.text = holder.context.getText(R.string.fpf_no_tags)
            tv.setTypeface(null, Typeface.ITALIC)
            holder.tagsLayout.addView(tv)
        }
        for (tag in visibleTags) {
            val tv = buildTagTextView(holder.context)
            tv.text = tag
            tv.background = ContextCompat.getDrawable(holder.context, R.drawable.fpf_tag_background)
            holder.tagsLayout.addView(tv)
        }
    }

    private fun buildTagTextView(context: Context): TextView {
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

    fun removeItem(adjPosition: Int) {
        feedEntries.removeAt(adjPosition)
        notifyItemRemoved(adjPosition)
    }

    override fun getItemCount() = feedEntries.size
}
