package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_image_info.*


private const val ARG_ENTRY = "entry"

class ImageInfoFragment : Fragment() {
    private var entry: FeedEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            entry = it.getParcelable(ARG_ENTRY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        fpf_info_name.text = entry!!.name
        fpf_info_date.text = entry!!.dateString
        insertTagsIntoLayout(fpf_info_tags_layout, context!!, entry!!.tags)
    }

    companion object {
        @JvmStatic
        fun newInstance(entry: FeedEntry) =
            ImageInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ENTRY, entry)
                }
            }
    }
}
