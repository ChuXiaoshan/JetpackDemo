package com.cxsplay.jpdemo.paging.bypage

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.cxsplay.jpdemo.paging.RedditApi
import com.cxsplay.jpdemo.paging.RedditPost
import java.util.concurrent.Executor

class SubRedditDataSourceFactory(
    private val redditApi: RedditApi,
    private val subredditName: String,
    private val retryExecutor: Executor) : DataSource.Factory<String, RedditPost>() {
    val sourceLiveData = MutableLiveData<PageKeyedSubredditDataSource>()
    override fun create(): DataSource<String, RedditPost> {
        val source = PageKeyedSubredditDataSource(redditApi, subredditName, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}