package com.dn.nhc.ui.common

import android.os.Bundle
import android.view.View
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresFragment
import com.dn.nhc.databinding.FragmentImageSliderBinding

val ARG_IMAGE = ""
class FragmentImageSlider :
    NeighborHoodChoresFragment<FragmentImageSliderBinding, CommonViewModel>(R.layout.fragment_image_slider) {

    private var image: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = it.getString(ARG_IMAGE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.image = this.image
    }

    companion object {
        @JvmStatic
        fun newInstance(image: String) =
            FragmentImageSlider().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE, image)
                }
            }
    }
}