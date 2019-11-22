package com.cxsplay.jpdemo.paging.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.cxsplay.jpdemo.R
import kotlinx.android.synthetic.main.activity_demo_list.*

class DemoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_list)
        init()
    }

    private fun init() {
        val adapter = CustomAdapter()
        rv.adapter = adapter

        val data = LivePagedListBuilder(
            CustomPageDataSourceFactory(DataRepository()), PagedList.Config.Builder()
                .setPageSize(20)
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(20)
                .build()
        ).build()

        data.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}
