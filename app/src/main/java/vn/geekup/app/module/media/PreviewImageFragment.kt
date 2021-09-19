package vn.geekup.app.module.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.geekup.app.databinding.FragmentPreviewImagesBinding

class PreviewImageFragment : Fragment() {

    private lateinit var fragmentBinding: FragmentPreviewImagesBinding
    private var url: String = ""

    companion object {
        private const val KEY_ARGUMENT_IMAGE = "key_argument_image"

        fun newInstance(imgUrl: String? = ""): PreviewImageFragment {
            val args = Bundle()
            args.putString(KEY_ARGUMENT_IMAGE, imgUrl)
            val fragment = PreviewImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentPreviewImagesBinding.inflate(layoutInflater, container, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = arguments?.getString(KEY_ARGUMENT_IMAGE) ?: ""
        fragmentBinding.url = url
    }

}