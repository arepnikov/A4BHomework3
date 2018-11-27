package com.daftmobile.a4bhomework3

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var EMAIL_RETRIEVER: EmailRetriever
        private val CONTACT_PICKER = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EMAIL_RETRIEVER = EmailRetriever.Impl(applicationContext)
        sendMailButton.setOnClickListener(this::sendMail)
    }

    // https://stackoverflow.com/questions/32954413/android-contact-picker-get-name-number-email
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK)
            return

        when (requestCode) {
            Companion.CONTACT_PICKER -> contactPickerResultHandler(data)
            else            -> showMsgDialog(this, getString(R.string.unexpected_request_code))
        }
    }

    private fun contactPickerResultHandler(data : Intent?) {
        extractEmailAddr(data).let { email_address ->
            if (email_address == null)
                showMsgDialog(this, getString(R.string.no_email_addr_error_msg))
            else
                sendMailViaApp(arrayOf(email_address))
        }
    }

    private fun createPickEmailAddrIntent() = Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.CommonDataKinds.Email.CONTENT_TYPE
    }

    // https://developer.android.com/guide/components/intents-common#ComposeEmail
    private fun createEmailSendToIntent(emails : Array<String>) = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse(getString(R.string.mailto_scheme))
        putExtra(Intent.EXTRA_EMAIL, emails)
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
    }

    private fun sendMail(view: View) = createPickEmailAddrIntent().let { pickAddrIntent ->
        if (pickAddrIntent.resolveActivity(packageManager) == null)
            showMsgDialog(this, getString(R.string.pick_contact_error_msg))
        else
            startActivityForResult(pickAddrIntent, Companion.CONTACT_PICKER)
    }

    private fun sendMailViaApp(emails: Array<String>) = createEmailSendToIntent(emails).let { sendEmailIntent ->
        if (sendEmailIntent.resolveActivity(packageManager) == null)
            showMsgDialog(this, getString(R.string.no_email_app_msg))
        else
            startActivity(sendEmailIntent)
    }

    private fun extractEmailAddr(data : Intent?)  = data?.data?.let { uri -> EMAIL_RETRIEVER.retrieve(uri) }
}
