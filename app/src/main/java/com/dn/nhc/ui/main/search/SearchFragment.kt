package com.dn.nhc.ui.main.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresFragment
import com.dn.nhc.databinding.FragmentSearchBinding
import com.dn.nhc.extention.toEditable
import com.dn.nhc.ui.search.SearchActivity
import com.lakue.lakue_library.ext.showKeyboard
import com.lakue.lakue_library.ext.showToast
import com.lakue.lakue_library.ext.startActivity
import com.lakue.lakue_library.ui.LakueActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment  : NeighborHoodChoresFragment<FragmentSearchBinding, SearchViewModel>(R.layout.fragment_search) {

    lateinit var searchAdapter: SearchAdapter

    companion object {

        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply{
            deleteKeywordEnent eventObserve {
                getRecentKeywords()
            }
            keywordEvent eventObserve {
                onShowKeywordResult(it)
            }
            liveKeyword.observe(viewLifecycleOwner){
                liveKeywordEmpty.postValue(it.isEmpty())
            }
        }

        searchAdapter = SearchAdapter(viewModel)

        getRecentKeywords()

        searchDone()
    }

    override fun onStart() {
        super.onStart()
        binding.etSearch.requestFocus();
        showKeyboard()
    }

    fun searchDone(){
        binding.apply{
            etSearch.setOnEditorActionListener { textView, actionId, KeyEvent ->
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    onShowKeywordResult()
                }
                false
            }
        }
    }

    fun saveDBKeyword(keyword: String){
        CoroutineScope(Dispatchers.Main).launch{
            viewModel.setKeyword(keyword)
            getRecentKeywords()
            binding.etSearch.text = "".toEditable()
        }
    }

    fun getRecentKeywords(){
        searchAdapter.clear()
        CoroutineScope(Dispatchers.Default).launch {
            val keywords = viewModel.getKeyword()
            if(keywords.isNotEmpty()){
                searchAdapter.addItem(0)
            }
            searchAdapter.addItem(keywords)
        }
    }

    fun onShowKeywordResult(keyword: String){
        if(keyword.trim().isEmpty()){
            showToast("검색 키워드를 입력해주세요.")
            return
        }
        saveDBKeyword(keyword)
        keyboardHide()
        (activity as LakueActivity<*, *>).startActivity<SearchActivity>(
            Pair(
                SearchActivity.EXTRA_SEARCH_KEYWORD, keyword
            )
        )
    }

    fun onShowKeywordResult(){
        val keyword = viewModel.liveKeyword.value!!
        if(keyword.trim().isEmpty()){
            showToast("검색 키워드를 입력해주세요.")
            return
        }
        saveDBKeyword(keyword)
        keyboardHide()
        (activity as LakueActivity<*, *>).startActivity<SearchActivity>(
            Pair(
                SearchActivity.EXTRA_SEARCH_KEYWORD, keyword
            )
        )
    }

    fun keyboardHide(){
        binding.etSearch.clearFocus()
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0);
    }
}