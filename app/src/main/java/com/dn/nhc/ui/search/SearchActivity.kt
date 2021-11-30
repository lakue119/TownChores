package com.dn.nhc.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivitySearchBinding
import com.dn.nhc.ui.search.content.SearchFragment
import com.dn.nhc.ui.search.hashtag.HashtagSearchFragment
import com.dn.nhc.utils.LoadingDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity :
    NeighborHoodChoresActivity<ActivitySearchBinding, SearchViewModel>(R.layout.activity_search) {

    val keyword by lazy { intent.getStringExtra(EXTRA_SEARCH_KEYWORD)}
    lateinit var searchFragmentAdapter: SearchFragmentAdapter

    companion object {
        val EXTRA_SEARCH_KEYWORD = "EXTRA_SEARCH_KEYWORD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LoadingDialog.showLoading(this@SearchActivity)
        viewModel.keyword = keyword!!

        searchFragmentAdapter = SearchFragmentAdapter(supportFragmentManager)


        binding.apply {
            headerTitle = "'${keyword}'에 대한 검색 결과"
            vpSearch.adapter = searchFragmentAdapter
            vpSearch.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    vpSearch.currentItem = tab?.position!!
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }

    }

    class SearchFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int = 2

        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> {
                    SearchFragment.newInstance()
                }
                else -> {
                    HashtagSearchFragment.newInstance()
                }
            }
        }
    }
}