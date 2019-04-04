package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage

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
        ItemTouchHelper(leftSwipeHandler).attachToRecyclerView(recyclerView)
//        val rightSwipeHandler = SwipeToDeleteCallback(ItemTouchHelper.RIGHT, viewAdapter)
//        ItemTouchHelper(rightSwipeHandler).attachToRecyclerView(recyclerView)

        // Add sample entries, because why not?
        viewAdapter.addItems(listOf(
            FeedEntry("https://i.redd.it/vmd69bdxvvo21.jpg", "Random /r/aww picture", "2019-03-28", this::runImageLabeling),
            FeedEntry("https://i.imgur.com/IWhT9DA.jpg", "Is this also a cat?", "2019-01-01", this::runImageLabeling),
            FeedEntry("https://i.redd.it/fxqfz6w62v821.jpg", "A longer title that hopefully wraps to the next line", "2019-03-01", this::runImageLabeling),
            FeedEntry("https://i.imgur.com/qWhyIxy.png", "I don't know what I'm doing", "2019-03-01", this::runImageLabeling)
        ))
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
                this::runImageLabeling
            )
            viewAdapter.addItem(entry)
            recyclerView.scrollToPosition(0)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun runImageLabeling(feedEntry: FeedEntry) {
        val vision = FirebaseVisionImage.fromBitmap(feedEntry.bitmap!!)
        val labeler = FirebaseVision.getInstance().onDeviceImageLabeler
        labeler.processImage(vision) // Task na osobnym wątku
            .addOnSuccessListener {
                feedEntry.tags = it.map { it.text }.toList()
                viewAdapter.updateItem(feedEntry)
            }
            .addOnFailureListener {
                Log.wtf("ImageRecognizer", it.message)
            }
    }
}
