package com.cxsplay.jpdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.BusUtils
import com.cxsplay.jpdemo.databinding.ActivityMainBinding
import com.cxsplay.jpdemo.paging.SimpleListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    @BusUtils.Bus(tag = "")
    private fun init() {
        bind.btnPaging.setOnClickListener {
            startActivity(Intent(this, SimpleListActivity::class.java))
        }
    }
}
