package pl.krzysztofwojciechowski.flickrpublicfeed

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


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
