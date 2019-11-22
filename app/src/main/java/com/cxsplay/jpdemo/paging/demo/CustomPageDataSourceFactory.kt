package com.cxsplay.jpdemo.paging.demo

import androidx.paging.DataSource

/**
 * Created by chuxiaoshan on 2019/11/22 10:30.
 *
 */
class CustomPageDataSourceFactory(private val repository: DataRepository) : DataSource.Factory<Int, DataBean>() {
    override fun create(): DataSource<Int, DataBean> {
        return CustomPageDataSource(repository)
    }
}