package com.goharsha.loadapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goharsha.loadapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

}
