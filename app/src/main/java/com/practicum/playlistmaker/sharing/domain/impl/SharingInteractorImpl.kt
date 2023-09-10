package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData


class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTermsOfService() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun writeToSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): Int {
        return R.string.link_to_the_course
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = R.string.mail_address,
            subject = R.string.mail_subject,
            message = R.string.mail_message
        )
    }

    private fun getTermsLink(): Int {
        return R.string.offer_link
    }
}