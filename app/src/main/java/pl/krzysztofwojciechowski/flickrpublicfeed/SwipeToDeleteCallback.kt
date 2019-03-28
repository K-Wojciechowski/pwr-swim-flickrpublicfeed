package pl.krzysztofwojciechowski.flickrpublicfeed

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SwipeToDeleteCallback(direction: Int, private val viewAdapter: FeedAdapter) :
    ItemTouchHelper.SimpleCallback(0, direction) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewAdapter.removeItem(viewHolder.adapterPosition)
    }
}
