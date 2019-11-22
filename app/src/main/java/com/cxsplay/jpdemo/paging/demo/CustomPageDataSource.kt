package com.cxsplay.jpdemo.paging.demo

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by chuxiaoshan on 2019/11/22 10:19.
 *
 */
class CustomPageDataSource(private val repository: DataRepository) : PageKeyedDataSource<Int, DataBean>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DataBean>) {

        GlobalScope.launch {
            delay(3000)
            val data = repository.loadData(params.requestedLoadSize)
            callback.onResult(data, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataBean>) {
        GlobalScope.launch {
            delay(3000)
            val data = repository.loadPageData(params.key, params.requestedLoadSize)
            data?.let { callback.onResult(data, params.key + 1) }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataBean>) {
        val data = repository.loadPageData(params.key, params.requestedLoadSize)
        data?.let { callback.onResult(data, params.key - 1) }
    }
}