package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import pl.krzysztofwojciechowski.flickrpublicfeed.detailsview.DetailsActivity
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FeedAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = FeedAdapter(this::showImageScreen)

        recyclerView = findViewById<RecyclerView>(R.id.fpf_recyclerview).apply {
            setHasFixedSize(false)

            layoutManager = viewManager
            adapter = viewAdapter
        }

        val leftSwipeHandler = SwipeToDeleteCallback(ItemTouchHelper.LEFT, viewAdapter)
        ItemTouchHelper(leftSwipeHandler).attachToRecyclerView(recyclerView)

        // Add sample entries, because why not?
        viewAdapter.addItems(listOf(
            makeEntry("https://i.redd.it/uj9kae4l7qx21.jpg", "A green bean for scale", "2019-05-12"),
            makeEntry("https://i.redd.it/vmd69bdxvvo21.jpg", "Duplicate", "2019-03-28"),
            makeEntry("https://i.redd.it/vmd69bdxvvo21.jpg", "Random /r/aww picture", "2019-03-28"),
            makeEntry("https://i.redd.it/ls4p7ztfenx21.jpg", "Is this also a cat?", "2019-01-01"),
            makeEntry("https://i.redd.it/fxqfz6w62v821.jpg", "A longer title that hopefully doesn’t break the layout", "2019-03-01"),
            makeEntry("https://i.redd.it/mp6zqzspgrx21.jpg", "I don't know what I'm doing", "2019-03-01")
        ))
    }

    private fun makeEntry(imageURL: String, name: String, date: String) =
        FeedEntry(imageURL, name, date, this::runImageLabeling)

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
            val entry = makeEntry(
                data.getStringExtra(ADD_INTENTEXTRA_IMAGE_URL),
                data.getStringExtra(ADD_INTENTEXTRA_NAME),
                data.getStringExtra(ADD_INTENTEXTRA_DATE)
            )
            viewAdapter.addItem(entry)
            recyclerView.scrollToPosition(0)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun runImageLabeling(feedEntry: FeedEntry) {
        if (feedEntry.bitmap == null) {
            feedEntry.tags = listOf(getString(R.string.fpf_no_image))
            viewAdapter.updateItem(feedEntry)
            return
        }
        val vision = FirebaseVisionImage.fromBitmap(feedEntry.bitmap!!)
        val labeler = FirebaseVision.getInstance().onDeviceImageLabeler
        labeler.processImage(vision) // Task na osobnym wątku
            .addOnSuccessListener {
                feedEntry.tags = it.map { tag -> tag.text }.toList()
                viewAdapter.updateItem(feedEntry)
            }
            .addOnFailureListener {
                Log.wtf("ImageRecognizer", it.message)
            }
    }

    private fun showImageScreen(entry: FeedEntry, similar: List<FeedEntry>) {
        val intent = Intent(applicationContext, DetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(DETAILS_INTENTEXTRA_ENTRY, entry)
        bundle.putParcelableArrayList(DETAILS_INTENTEXTRA_SIMILAR, similar as ArrayList<out Parcelable>)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
