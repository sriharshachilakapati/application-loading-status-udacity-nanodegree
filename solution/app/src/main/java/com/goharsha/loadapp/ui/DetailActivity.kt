package com.goharsha.loadapp.ui

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goharsha.loadapp.R
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

        with(binding.contentDetail) {
            if (downloadDetails.status == DownloadManager.STATUS_FAILED) {
                statusCard.setCardBackgroundColor(getColor(R.color.failureCardColor))
                statusIcon.setImageResource(R.drawable.ic_error)
                statusIcon.contentDescription = getString(R.string.download_failed_simple)
            } else {
                statusCard.setCardBackgroundColor(getColor(R.color.successCardColor))
                statusIcon.setImageResource(R.drawable.ic_success)
                statusIcon.contentDescription = getString(R.string.download_succeeded)
            }

            downloadTitle.setText(downloadDetails.title ?: "")
            downloadId.setText(downloadDetails.id.toString())
        }

        binding.closeFab.setOnClickListener { onCloseFABClicked() }
    }

    private fun onCloseFABClicked() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        startActivity(intent)
        finish()
    }
}
