package com.cxsplay.jpdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.cxsplay.jpdemo.databinding.ActivityMainBinding
import com.cxsplay.jpdemo.paging.RedditPostRepository
import com.cxsplay.jpdemo.paging.SimpleListActivity
import com.cxsplay.jpdemo.paging.demo.DemoListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        bind.btnPaging.setOnClickListener {
            val intent = SimpleListActivity.intentFor(this, RedditPostRepository.Type.IN_MEMORY_BY_PAGE)
            startActivity(intent)
        }
        bind.btnPaging1.setOnClickListener { startActivity(Intent(this, DemoListActivity::class.java))}
    }
}
