package com.dn.nhc.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityIntroBinding
import com.dn.nhc.ui.login.LoginActivity
import com.lakue.lakue_library.ext.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity :
    NeighborHoodChoresActivity<ActivityIntroBinding, IntroViewModel>(R.layout.activity_intro) {
    val TAG = "IntroActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pagerAdapter = IntroSlidePagerAdapter(supportFragmentManager)
        binding.apply {
            vpIntro.adapter = pagerAdapter
            dotsIndicator.setViewPager(vpIntro)
        }
        setViewPagerEvent()
        viewModel.setIsHideIntro()
    }

    private fun setViewPagerEvent() {
        binding.apply {

            vpIntro.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    isLastPage = position == 3
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
        }
    }

    fun onNextIntro() {
        binding.vpIntro.apply {
            currentItem += 1
        }
    }

    fun onNextActivity() {
        startActivity<LoginActivity>()
        finish()
    }

    private inner class IntroSlidePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = 4

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    return IntroSlidePagerFragment.newInstance(
                        introRes = R.drawable.intro1,
                        introTitle = getString(R.string.intro_step1_title),
                        introContent = getString(R.string.intro_step1_content)
                    )
                }
                1 -> {
                    return IntroSlidePagerFragment.newInstance(
                        introRes = R.drawable.intro2,
                        introTitle = getString(R.string.intro_step2_title),
                        introContent = getString(R.string.intro_step2_content)
                    )
                }
                2 -> {
                    return IntroSlidePagerFragment.newInstance(
                        introRes = R.drawable.intro3,
                        introTitle = getString(R.string.intro_step3_title),
                        introContent = getString(R.string.intro_step3_content)
                    )
                }
                else -> {
                    return IntroSlidePagerFragment.newInstance(
                        introRes = R.drawable.intro4,
                        introTitle = getString(R.string.intro_step4_title),
                        introContent = getString(R.string.intro_step4_content)
                    )
                }
            }
        }
    }
}