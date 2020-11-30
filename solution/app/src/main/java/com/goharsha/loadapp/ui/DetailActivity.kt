package com.goharsha.loadapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goharsha.loadapp.databinding.ActivityDetailBinding
import com.goharsha.loadapp.utils.DownloadDetails
import com.goharsha.loadapp.utils.cancelNotification

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DETAILS = "details"
    }

    private lateinit var downloadDetails: DownloadDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        downloadDetails = intent.getParcelableExtra(EXTRA_DETAILS) ?: return
        cancelNotification(downloadDetails.id)
    }
}
