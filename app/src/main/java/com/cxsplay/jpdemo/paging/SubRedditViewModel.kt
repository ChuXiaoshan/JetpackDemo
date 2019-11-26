package com.cxsplay.jpdemo.paging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils

class SubRedditViewModel(private val repository: RedditPostRepository) : ViewModel() {
    private val subredditName = MutableLiveData<String>()
    private val repoResult = map(subredditName) {
        repository.postsOfSubreddit(it, 20)
    }

    val posts = switchMap(repoResult) {
        it.pagedList
    }
    val networkState = switchMap(repoResult) {
        it.networkState
    }
    val refreshState = switchMap(repoResult) {
        it.refreshState
    }

    fun refresh() {
        LogUtils.d("---refresh--->")
        repoResult.value?.refresh?.invoke()
    }

    fun showSubreddit(subreddit: String): Boolean {
        if (subredditName.value == subreddit) {
            return false
        }
        subredditName.value = subreddit
        return true
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }

    fun currentSubreddit(): String? = subredditName.value
}