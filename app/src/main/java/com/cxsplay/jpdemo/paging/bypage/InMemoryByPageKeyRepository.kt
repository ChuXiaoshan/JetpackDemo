package com.cxsplay.jpdemo.paging.bypage

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.cxsplay.jpdemo.paging.Listing
import com.cxsplay.jpdemo.paging.RedditApi
import com.cxsplay.jpdemo.paging.RedditPost
import com.cxsplay.jpdemo.paging.RedditPostRepository
import java.util.concurrent.Executor

class InMemoryByPageKeyRepository(private val redditApi: RedditApi, private val networkExecutor: Executor) : RedditPostRepository {

    @MainThread
    override fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<RedditPost> {
        val sourceFactory = SubRedditDataSourceFactory(redditApi, subReddit, networkExecutor)
        val livePageList = sourceFactory.toLiveData(pageSize = pageSize, fetchExecutor = networkExecutor)

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
            pagedList = livePageList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState },
            retry = { sourceFactory.sourceLiveData.value?.retryAllFailed() },
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() },
            refreshState = refreshState
        )
    }
}