package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigator(private val context: Context) {

    fun shareLink(link: Int) {
        val intent = Intent(Intent.ACTION_SEND)
        val shLink = context.getString(link)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shLink)
        val sharingTitle = context.getString(R.string.sharing_text)
        val chooserIntent = Intent.createChooser(intent, sharingTitle)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.context.startActivity(chooserIntent)
    }

    fun openLink(url: Int) {
        val urlResourceId = context.getString(url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlResourceId))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.context.startActivity(intent)
    }

    fun openEmail(emailData: EmailData) {
        val emailAddress = context.getString(emailData.email)
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(emailData.subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(emailData.message))
        }

        val sendTitle = context.getString(R.string.send_message_title)
        val chooserIntent = Intent.createChooser(intent, sendTitle)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.context.startActivity(chooserIntent)
    }
}