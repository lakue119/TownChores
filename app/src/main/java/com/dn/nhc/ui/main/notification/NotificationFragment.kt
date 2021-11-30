package com.dn.nhc.ui.main.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresFragment
import com.dn.nhc.databinding.FragmentNotificationBinding
import com.dn.nhc.remote.model.PushesResponse
import com.dn.nhc.remote.model.common.Pushes
import com.dn.nhc.ui.comment.CommentActivity
import com.dn.nhc.ui.comment.CommentActivity.Companion.EXTRA_COMMENT_ID
import com.dn.nhc.utils.LoadingDialog
import com.lakue.lakue_library.ui.LakueActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    NeighborHoodChoresFragment<FragmentNotificationBinding, NotificationViewModel>(R.layout.fragment_notification),
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var notificationAdapter: NotificationAdapter
    private var page = 1
    val ROAD_COUNT = 20

    val rvBottomCatch: Function1<Int, Unit> = this::onBottomCatch

    companion object {

        @JvmStatic
        fun newInstance() =
            NotificationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationAdapter = NotificationAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.apply {
            showDetailEvent eventObserve { item ->
                showDetailPusher(item.first, item.second)
            }
            if(isInit){
                getNotificationList()
                isInit = false
            }
        }

        setObserver()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            srlNotification.setOnRefreshListener(this@NotificationFragment)
            ncRvAlram.setEmptyView(binding.includeEmpty.clEmpty)
        }
    }

    //RecyclerView Bottom Catch
    private fun onBottomCatch(lastPosition: Int) {
        if (!rvloading && lastPosition >= notificationAdapter.itemCount - 2) {
            getNotificationList()
        }
    }

    private fun setObserver() {
        viewModel.apply {
            liveSuccess.observe(viewLifecycleOwner) { response ->
                if(response == null){
                    return@observe
                }
                when(response){
                    is PushesResponse -> {

                        LoadingDialog.hideLoading(requireActivity() as LakueActivity<*, *>)
                        val pushes = response.response?.pushes!!
                        notificationAdapter.addItem(pushes)
                        binding.ncRvAlram.setIsDataSetting(true)
                        if (pushes.isEmpty()) isLastPage = true
                        page++
                        rvloading = false
                    }
                }
            }
        }
    }

    fun showDetailPusher(pushes: Pushes, position: Int) {
        when(pushes.msgDivision){
            1 -> {
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra(EXTRA_COMMENT_ID, pushes.commentId)
                startActivity(intent)
            }
        }

        notificationAdapter.readPusher(position)
    }

    fun getNotificationList() {
        //마지막페이지면 return
        if(isLastPage) {
            return
        }
        rvloading = true
        LoadingDialog.showLoading(requireActivity() as LakueActivity<*, *>)
        viewModel.getNotificationList(
            ROAD_COUNT,
            page
        )
    }

    override fun onRefresh() {
        notificationAdapter.clear()
        page = 1
        isLastPage = false
        getNotificationList()
        binding.srlNotification.isRefreshing = false
    }

}