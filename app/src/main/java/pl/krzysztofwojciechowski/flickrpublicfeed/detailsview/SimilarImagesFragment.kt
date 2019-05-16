package pl.krzysztofwojciechowski.flickrpublicfeed.detailsview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_similar_images.*
import pl.krzysztofwojciechowski.flickrpublicfeed.FeedEntry

import pl.krzysztofwojciechowski.flickrpublicfeed.R

private const val ARG_SIMILAR = "similar"

class SimilarImagesFragment : Fragment() {
    private lateinit var similar: ArrayList<FeedEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            similar = it.getParcelableArrayList<FeedEntry>(ARG_SIMILAR) as ArrayList<FeedEntry>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_similar_images, container, false)
    }

    override fun onStart() {
        super.onStart()
        val gridAdapter = SimilarImagesGridAdapter(context!!, similar)

        fpf_similar_grid.adapter = gridAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(similar: ArrayList<FeedEntry>) =
            SimilarImagesFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_SIMILAR, similar)
                }
            }
    }
}
