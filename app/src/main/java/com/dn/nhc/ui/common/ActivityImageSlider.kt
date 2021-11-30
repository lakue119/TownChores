package com.dn.nhc.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityImageSliderBinding
import com.dn.nhc.remote.model.common.Image
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityImageSlider :
    NeighborHoodChoresActivity<ActivityImageSliderBinding, CommonViewModel>(R.layout.activity_image_slider) {

    private lateinit var adapter: AdapterImageSliderViewPager

    private val imgs by lazy { intent.getSerializableExtra(EXTRA_IMGS) as ArrayList<Image> }
    private val pos by lazy { intent.getIntExtra(EXTRA_POSITION,0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val images = ArrayList<String>()
        for(image in imgs){
            images.add(image.imageNameUrl!!)
        }

        adapter = AdapterImageSliderViewPager(supportFragmentManager, images)

        binding.apply{
            viewPager.adapter = adapter
            viewPager.currentItem = pos
            tvCount.text = "${pos+1}"
            tvMax.text = " / ${imgs.size}"
        }

        widgetEventListener()
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    //위젯 이벤트 리스너
    private fun widgetEventListener(){
        binding.apply{
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(i: Int, v: Float, i1: Int) {

                }

                override fun onPageSelected(i: Int) {
                    tvCount.text = "${i+1}"
                }

                override fun onPageScrollStateChanged(i: Int) {

                }
            })

            ivClose.setOnClickListener{
                finish()
            }
        }
    }

    companion object {

        internal const val EXTRA_IMGS = "images"
        internal const val EXTRA_POSITION = "position"
    }

    class AdapterImageSliderViewPager(fm: FragmentManager?, images: ArrayList<String>) :
        FragmentPagerAdapter(fm!!) {
        var images = ArrayList<String>()
        override fun getItem(i: Int): Fragment {
            return FragmentImageSlider.newInstance(images[i])
        }

        override fun getCount(): Int {
            return images.size
        }

        init {
            this.images = images
        }
    }

}