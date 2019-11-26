package com.cxsplay.jpdemo.paging.bypage

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.blankj.utilcode.util.LogUtils
import com.cxsplay.jpdemo.paging.NetworkState
import com.cxsplay.jpdemo.paging.RedditApi
import com.cxsplay.jpdemo.paging.RedditPost
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

class PageKeyedSubredditDataSource(
    private val redditApi: RedditApi,
    private val subredditName: String,
    private val retryExecutor: Executor
) : PageKeyedDataSource<String, RedditPost>() {

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, RedditPost>) {
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, RedditPost>) {
        networkState.postValue(NetworkState.LOADING)
        redditApi.getTopAfter(subredditName, params.key, params.requestedLoadSize).enqueue(
            object : retrofit2.Callback<RedditApi.ListingResponse> {
                override fun onFailure(call: Call<RedditApi.ListingResponse>, t: Throwable) {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                }

                override fun onResponse(call: Call<RedditApi.ListingResponse>, response: Response<RedditApi.ListingResponse>) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        val items = data?.children?.map { it.data } ?: emptyList()
                        retry = null
                        callback.onResult(items, data?.after)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        retry = { loadAfter(params, callback) }
                        networkState.postValue(NetworkState.error("error code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, RedditPost>) {
        LogUtils.d("---loadInitial--->")
        val request = redditApi.getTop(subredditName, params.requestedLoadSize)
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)
        try {
            val response = request.execute()
            val data = response.body()?.data
            val items = data?.children?.map { it.data } ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items, data?.before, data?.after)
        } catch (e: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(e.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }
}