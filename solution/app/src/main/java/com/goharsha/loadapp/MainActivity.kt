package com.goharsha.loadapp

import android.app.DownloadManager
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
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import com.goharsha.loadapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0
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
                if (buttonState == ButtonState.DOWNLOAD) {
                    download()
                }
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
            if (downloadID != intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
                return
            }

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query().setFilterById(downloadID)
            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))

                val notificationMessage = if (status == DownloadManager.STATUS_FAILED) {
                    getString(R.string.download_failed, title)
                } else {
                    getString(R.string.download_success, title)
                }

                val notifyIntent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtras(
                        bundleOf(
                            "status" to status,
                            "title" to title,
                            "notificationId" to downloadID.toInt()
                        )
                    )
                }

                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                showNotification(
                    R.id.channel_download_completed,
                    downloadID.toInt(),
                    getString(R.string.download_notification_title),
                    notificationMessage,
                    getString(R.string.action_show_details),
                    pendingIntent
                )
            }

            binding.contentMain.customButton.buttonState = ButtonState.DOWNLOAD
        }
    }

    private fun download() {
        if (urlToDownload == null) {
            Toast.makeText(this, getString(R.string.message_no_option_selected), Toast.LENGTH_SHORT)
                .show()
            return
        }

        binding.contentMain.customButton.buttonState = ButtonState.DOWNLOADING

        val selectedOption =
            findViewById<RadioButton>(binding.contentMain.radioGroup.checkedRadioButtonId)

        val request = DownloadManager.Request(Uri.parse(urlToDownload))
            .setTitle(selectedOption.text)
            .setDescription(getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }
}
