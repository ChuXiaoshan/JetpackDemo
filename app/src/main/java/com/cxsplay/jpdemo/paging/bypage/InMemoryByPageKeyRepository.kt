package com.cxsplay.jpdemo.paging.bypage

import androidx.lifecycle.Transformations
import com.cxsplay.jpdemo.paging.Listing
import com.cxsplay.jpdemo.paging.RedditApi
import com.cxsplay.jpdemo.paging.RedditPost
import com.cxsplay.jpdemo.paging.RedditPostRepository
import java.util.concurrent.Executor

class InMemoryByPageKeyRepository(private val redditApi: RedditApi, private val networkExecutor: Executor) : RedditPostRepository {

    override fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<RedditPost> {
        val sourceFactory = SubRedditDataSourceFactory(redditApi, subReddit, networkExecutor)
        val livePageList = sourceFactory.toLiveData(pageSize, networkExecutor)

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
            livePageList,
            Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            sourceFactory.sourceLiveData.value?.retryAllFailed(),
            sourceFactory.sourceLiveData.value?.invalidate(),
            refreshState
        )
    }
}