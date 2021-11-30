package com.dn.nhc.ui.search.content

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresFragment
import com.dn.nhc.databinding.FragmentContentSearchBinding
import com.dn.nhc.global.NowLocation
import com.dn.nhc.remote.model.PheedResponse
import com.dn.nhc.ui.pheed.detail.PheedDetailActivity
import com.dn.nhc.ui.search.SearchAdapter
import com.dn.nhc.ui.search.SearchViewModel
import com.dn.nhc.utils.LoadingDialog
import com.lakue.lakue_library.ext.startActivity
import com.lakue.lakue_library.ui.LakueActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment :
    NeighborHoodChoresFragment<FragmentContentSearchBinding, SearchViewModel>(R.layout.fragment_content_search),
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var searchPheedAdapter: SearchAdapter
    private val parentViewModel: SearchViewModel by activityViewModels()
    private var page = 1
    val ROAD_COUNT = 20

    val rvBottomCatch: Function1<Int, Unit> = this::onBottomCatch

    companion object {

        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchPheedAdapter = SearchAdapter(viewModel)

        binding.apply {
            srlPheed.setOnRefreshListener(this@SearchFragment)
            ncRvPheed.setEmptyView(binding.includeEmpty.clEmpty)
        }

        viewModel.apply {
            showDetailEvent eventObserve { item ->
                showDetailPheed(item.id!!)
            }
            getPheedList()
        }

        setObserver()
    }

    //RecyclerView Bottom Catch
    private fun onBottomCatch(lastPosition: Int) {
        if (!rvloading && lastPosition >= searchPheedAdapter.itemCount - 2) {
            getPheedList()
        }
    }

    private fun setObserver() {
        viewModel.apply {
            liveSuccess.observe(viewLifecycleOwner) { response ->
                if(response == null){
                    return@observe
                }
                LoadingDialog.hideLoading(requireActivity() as LakueActivity<*, *>)
                val data = response as PheedResponse
                val pheeds = data.response.posts
                searchPheedAdapter.addItem(pheeds)
                binding.ncRvPheed.setIsDataSetting(true)
                if (pheeds.isEmpty()) isLastPage = true
                page++
                rvloading = false
            }
        }
    }

    fun showDetailPheed(pheedId: Long) {
        (requireActivity() as LakueActivity<*, *>).startActivity<PheedDetailActivity>(
            Pair(
                PheedDetailActivity.EXTRA_PHEED_ID, pheedId.toString()
            )
        )
    }

    fun getPheedList() {
        //마지막페이지면 return
        if(isLastPage) {
            return
        }
        rvloading = true
        viewModel.getSearchContentPheedList(
            parentViewModel.keyword,
            ROAD_COUNT,
            page,
            NowLocation.latitude,
            NowLocation.longitude
        )
    }

    override fun onRefresh() {
        searchPheedAdapter.clear()
        page = 1
        isLastPage = false
        getPheedList()
        binding.srlPheed.isRefreshing = false
    }

}