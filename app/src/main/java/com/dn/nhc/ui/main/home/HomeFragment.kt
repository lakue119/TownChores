package com.dn.nhc.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.base.NeighborHoodChoresFragment
import com.dn.nhc.databinding.FragmentHomeBinding
import com.dn.nhc.global.NowLocation
import com.dn.nhc.remote.model.PheedResponse
import com.dn.nhc.ui.pheed.detail.PheedDetailActivity
import com.dn.nhc.ui.search.SearchActivity
import com.dn.nhc.utils.LoadingDialog
import com.dn.nhc.utils.NeighborHoodChoresUtils
import com.lakue.lakue_library.ext.startActivity
import com.lakue.lakue_library.ui.LakueActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    NeighborHoodChoresFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home),
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var pheedAdapter: HomeAdapter
    private var page = 1
    val ROAD_COUNT = 20

    val rvBottomCatch: Function1<Int, Unit> = this::onBottomCatch

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pheedAdapter = HomeAdapter(viewModel)
        LoadingDialog.showLoading(requireActivity() as LakueActivity<*, *>)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.apply {
            showDetailEvent eventObserve { item ->
                showDetailPheed(item.id!!)
            }
            hashtagClickEvent eventObserve { keyword ->
                (requireActivity() as LakueActivity<*, *>).startActivity<SearchActivity>(
                    Pair(
                        SearchActivity.EXTRA_SEARCH_KEYWORD, keyword
                    )
                )
            }
            if(isInit){
                getPheedList()
                isInit = false
            }
        }

        setObserver()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            srlPheed.setOnRefreshListener(this@HomeFragment)
            ncRvPheed.setEmptyView(binding.includeEmpty.clEmpty)
//            fabScrollTop.scaleType = ImageView.ScaleType.CENTER
        }
    }

    //RecyclerView Bottom Catch
    private fun onBottomCatch(lastPosition: Int) {
        Log.i("QWKLRJQWKLRJ", "===========================================")
        Log.i("QWKLRJQWKLRJ", "rvloading ${rvloading}")
        Log.i("QWKLRJQWKLRJ", "lastPosition ${lastPosition}")
        Log.i("QWKLRJQWKLRJ", "itemCount ${pheedAdapter.itemCount}")
        Log.i("QWKLRJQWKLRJ", "isLastPage ${isLastPage}")
        if (pheedAdapter.itemCount > 0 && !rvloading && lastPosition >= pheedAdapter.itemCount - 2) {
            getPheedList()
        }
    }

    private fun setObserver() {

        NeighborHoodChoresUtils.liveRefreshHome.observe(viewLifecycleOwner) {
            if(it && !rvloading){
                pheedAdapter.clear()
                page = 1
                isLastPage = false
                getPheedList()
                binding.srlPheed.isRefreshing = false
            }
            NeighborHoodChoresUtils.liveRefreshHome.postValue(false)
        }

        viewModel.apply {
            liveSuccess.observe(viewLifecycleOwner) { response ->
                rvloading = false
                if(response == null){
                    return@observe
                }
                LoadingDialog.hideLoading(requireActivity() as LakueActivity<*, *>)
                val data = response as PheedResponse
                val pheeds = data.response.posts
                pheedAdapter.addItem(pheeds)
                binding.ncRvPheed.setIsDataSetting(true)
                if (pheeds.isEmpty()) isLastPage = true
                page++
            }
        }
    }

    fun showDetailPheed(pheedId: Long) {
        (activity as LakueActivity<*, *>).startActivity<PheedDetailActivity>(
            Pair(
                PheedDetailActivity.EXTRA_PHEED_ID, pheedId.toString()
            )
        )
    }

    fun onScrollTop(){
        binding.ncRvPheed.smoothScrollToPosition(0)
    }

    fun getPheedList() {
        //마지막페이지면 return
        if(isLastPage) {
            return
        }
        rvloading = true
        viewModel.getPheedList(
            ROAD_COUNT,
            page,
            NowLocation.latitude,
            NowLocation.longitude
        )
    }

    override fun onRefresh() {
        binding.ncRvPheed.setIsDataSetting(false)
        pheedAdapter.clear()
        page = 1
        isLastPage = false
        CoroutineScope(Dispatchers.Main).launch {
            (requireActivity() as NeighborHoodChoresActivity<*, *>).getLocation { latitude, longitude, area ->
                NowLocation.latitude = latitude
                NowLocation.longitude = longitude
                NowLocation.area = area

                getPheedList()
                binding.srlPheed.isRefreshing = false
            }
        }
    }
}