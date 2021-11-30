package com.dn.nhc.ui.intro

import android.os.Bundle
import android.view.View
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresFragment
import com.dn.nhc.databinding.FragmentIntroSlidePagerBinding

private const val ARG_INTRO_IMAGE = "intro_image"
private const val ARG_INTRO_TITLE = "intro_title"
private const val ARG_INTRO_CONTENT = "intro_content"

class IntroSlidePagerFragment :
    NeighborHoodChoresFragment<FragmentIntroSlidePagerBinding, IntroSliderPagerViewModel>(R.layout.fragment_intro_slide_pager) {

    private var introImage: Int? = null
    private var introTitle: String? = null
    private var introContent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            introImage = it.getInt(ARG_INTRO_IMAGE)
            introTitle = it.getString(ARG_INTRO_TITLE)
            introContent = it.getString(ARG_INTRO_CONTENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            image = introImage
            title = introTitle
            content = introContent
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(introRes: Int, introTitle: String, introContent: String) =
            IntroSlidePagerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INTRO_IMAGE, introRes)
                    putString(ARG_INTRO_TITLE, introTitle)
                    putString(ARG_INTRO_CONTENT, introContent)
                }
            }
    }
}