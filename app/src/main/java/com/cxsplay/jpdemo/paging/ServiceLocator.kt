package com.cxsplay.jpdemo.paging

import android.app.Application
import android.content.Context
import com.cxsplay.jpdemo.paging.bypage.InMemoryByPageKeyRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application,
                        useInMemoryDB = false
                    )
                }
                return instance!!
            }
        }
    }

    fun getRepository(type: RedditPostRepository.Type): RedditPostRepository

    fun getNetWorkExecutor(): Executor

    fun getDiskIOExecutor(): Executor

    fun getRedditApi(): RedditApi
}

open class DefaultServiceLocator(val app: Application, val useInMemoryDB: Boolean) : ServiceLocator {

    private val DISK_IO = Executors.newSingleThreadExecutor()

    private val NETWORK_IO = Executors.newSingleThreadExecutor()

    private val db by lazy {
        //        RedditDb.create(app, useInMemoryDB)
    }

    private val api by lazy {
        RedditApi.create()
    }

    override fun getRepository(type: RedditPostRepository.Type): RedditPostRepository {
        return InMemoryByPageKeyRepository(getRedditApi(), getNetWorkExecutor())
//        when (type) {
//            RedditPostRepository.Type.IN_MEMORY_BY_ITEM -> InMemoryByItemRepository(getRedditApi(), getNetWorkExecutor())
//        RedditPostRepository.Type.IN_MEMORY_BY_PAGE ->
//        InMemoryByPageKeyRepository(getRedditApi(), getNetWorkExecutor())
//            RedditPostRepository.Type.DB -> DbRedditPostRepository(db, getRedditApi(), getDiskIOExecutor())
//        }
    }

    override fun getNetWorkExecutor(): Executor = NETWORK_IO
    override fun getDiskIOExecutor(): Executor = DISK_IO
    override fun getRedditApi(): RedditApi = api
}