package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var binding: ActivityMainBinding

    private val downloadOptions = mutableMapOf<Int, String>()

    private val urlToDownload: String?
        get() = downloadOptions[binding.contentMain.radioGroup.checkedRadioButtonId]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createDownloadOptions()

        with (binding.contentMain.customButton) {
            buttonState = ButtonState.LOADING

            setOnClickListener {
                download()
            }
        }
    }

    private fun createDownloadOptions() {
        val group = binding.contentMain.radioGroup
        val optionNames = resources.getStringArray(R.array.download_option_names)
        val optionUrls = resources.getStringArray(R.array.download_option_urls)

        optionNames.forEachIndexed { index, text ->
            val viewParams = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                topMargin = 10.dp.toInt()
                bottomMargin = 10.dp.toInt()
            }

            val buttonId = ViewCompat.generateViewId()
            val radioButton = AppCompatRadioButton(this)

            radioButton.text = text
            radioButton.id = buttonId
            radioButton.layoutParams = viewParams

            downloadOptions[buttonId] = optionUrls[index]

            group.addView(radioButton)
        }

        group.setOnCheckedChangeListener { _, _ ->
            Toast.makeText(this, urlToDownload, Toast.LENGTH_SHORT).show()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {
        if (urlToDownload == null) {
            Toast.makeText(this, getString(R.string.message_no_option_selected), Toast.LENGTH_SHORT).show()
            return
        }

        val request = DownloadManager.Request(Uri.parse(urlToDownload))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }
}
