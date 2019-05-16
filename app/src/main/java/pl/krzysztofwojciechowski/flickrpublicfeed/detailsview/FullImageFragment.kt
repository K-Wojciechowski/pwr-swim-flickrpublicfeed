package pl.krzysztofwojciechowski.flickrpublicfeed.detailsview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_full_image.*
import pl.krzysztofwojciechowski.flickrpublicfeed.R
import pl.krzysztofwojciechowski.flickrpublicfeed.getPicassoCreator
import pl.krzysztofwojciechowski.flickrpublicfeed.getPicassoImageOrPlaceholder


private const val ARG_IMAGE_URL = "imageUrl"

class FullImageFragment : Fragment() {
    private var imageURL: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageURL = arguments?.getString(ARG_IMAGE_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_image, container, false)
    }

    override fun onStart() {
        super.onStart()
        getPicassoImageOrPlaceholder(getPicassoCreator(imageURL!!), fpf_full_image_iv)
    }

    companion object {
        @JvmStatic
        fun newInstance(imageURL: String) =
            FullImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URL, imageURL)
                }
            }
    }
}
