package pl.krzysztofwojciechowski.flickrpublicfeed

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*

class DetailsActivity : AppCompatActivity() {
    var isShowingInfoScreen = false
    var entry: FeedEntry? = null
    var similar: List<FeedEntry>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        intent.extras?.apply {
            entry = getParcelable(DETAILS_INTENTEXTRA_ENTRY) as FeedEntry
            similar =
                getParcelableArrayList<FeedEntry>(DETAILS_INTENTEXTRA_SIMILAR) as ArrayList<FeedEntry>
        }

        supportActionBar!!.title = entry!!.name
        isShowingInfoScreen = false
        fpf_details_bottom.visibility = View.GONE

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.fpf_details_top, FullImageFragment.newInstance(entry!!.imageURL))
        ft.add(R.id.fpf_details_bottom, PlaceholderFragment())
        ft.commit()
    }

    fun changeMode() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (isShowingInfoScreen) {
            isShowingInfoScreen = false
            fpf_details_bottom.visibility = View.GONE
            ft.replace(R.id.fpf_details_top, FullImageFragment.newInstance(entry!!.imageURL))
        } else {
            isShowingInfoScreen = true
            fpf_details_bottom.visibility = View.VISIBLE
            ft.replace(R.id.fpf_details_top, ImageInfoFragment.newInstance(entry!!))
        }
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.fpf_menu_change_screen) {
            changeMode()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
