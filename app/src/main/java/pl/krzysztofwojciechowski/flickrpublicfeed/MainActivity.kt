package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FeedAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = FeedAdapter()

        recyclerView = findViewById<RecyclerView>(R.id.fpf_recyclerview).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(false)

            layoutManager = viewManager
            adapter = viewAdapter
        }

        val leftSwipeHandler = SwipeToDeleteCallback(ItemTouchHelper.LEFT, viewAdapter)
        val rightSwipeHandler = SwipeToDeleteCallback(ItemTouchHelper.RIGHT, viewAdapter)
        ItemTouchHelper(leftSwipeHandler).attachToRecyclerView(recyclerView)
//        ItemTouchHelper(rightSwipeHandler).attachToRecyclerView(recyclerView)

        // Add sample entries, because why not?
        viewAdapter.addItem(FeedEntry("https://i.redd.it/vmd69bdxvvo21.jpg", "Random /r/aww picture", "2019-03-28", "one, two, three, four"))
        viewAdapter.addItem(FeedEntry("https://i.redd.it/fxqfz6w62v821.jpg", "A longer title that hopefully wraps to the next line", "2019-03-01", "this is very long and should show the horizontal scroll bar. more text, even more? hi?"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.fpf_menu_add -> {
                val addActivity = Intent(this, AddActivity::class.java)
                startActivityForResult(addActivity, REQUEST_CODE_ADD)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK && data != null) {
            val entry = FeedEntry(
                data.getStringExtra(INTENTEXTRA_IMAGE_URL),
                data.getStringExtra(INTENTEXTRA_NAME),
                data.getStringExtra(INTENTEXTRA_DATE),
                data.getStringExtra(INTENTEXTRA_TAGS)
            )
            viewAdapter.addItem(entry)
            recyclerView.scrollToPosition(0)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}