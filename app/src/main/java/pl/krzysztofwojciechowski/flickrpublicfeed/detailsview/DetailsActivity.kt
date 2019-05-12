package pl.krzysztofwojciechowski.flickrpublicfeed.detailsview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import pl.krzysztofwojciechowski.flickrpublicfeed.DETAILS_INTENTEXTRA_ENTRY
import pl.krzysztofwojciechowski.flickrpublicfeed.DETAILS_INTENTEXTRA_SIMILAR
import pl.krzysztofwojciechowski.flickrpublicfeed.FeedEntry
import pl.krzysztofwojciechowski.flickrpublicfeed.R

class DetailsActivity : AppCompatActivity() {
    var currentMode = DetailsMode.FULLSCREEN
    lateinit var entry: FeedEntry
    lateinit var similar: ArrayList<FeedEntry>

    lateinit var fullImageFragment: FullImageFragment
    lateinit var imageInfoFragment: ImageInfoFragment
    lateinit var similarImagesFragment: SimilarImagesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        intent.extras?.apply {
            entry = getParcelable(DETAILS_INTENTEXTRA_ENTRY) as FeedEntry
            similar = getParcelableArrayList<FeedEntry>(DETAILS_INTENTEXTRA_SIMILAR) as ArrayList<FeedEntry>
        }

        supportActionBar!!.title = entry.name
        currentMode = DetailsMode.FULLSCREEN

        fullImageFragment = FullImageFragment.newInstance(entry.imageURL)
        imageInfoFragment = ImageInfoFragment.newInstance(entry)
        similarImagesFragment = SimilarImagesFragment.newInstance(similar)

        changeMode(DetailsMode.FULLSCREEN)
    }

    private fun changeMode(newMode: DetailsMode = DetailsMode.AUTO) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (newMode == DetailsMode.INFO || (newMode == DetailsMode.AUTO && currentMode == DetailsMode.INFO)) {
            currentMode = DetailsMode.FULLSCREEN
            ft.replace(R.id.fpf_details_top, fullImageFragment)
            ft.remove(similarImagesFragment)
            ft.runOnCommit { fpf_details_bottom.visibility = View.GONE }
        } else {
            currentMode = DetailsMode.INFO
            ft.replace(R.id.fpf_details_top, imageInfoFragment)
            ft.replace(R.id.fpf_details_bottom, similarImagesFragment)
            ft.runOnCommit { fpf_details_bottom.visibility = View.VISIBLE }
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

enum class DetailsMode {
    AUTO, FULLSCREEN, INFO
}
