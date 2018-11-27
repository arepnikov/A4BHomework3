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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EMAIL_RETRIEVER = EmailRetriever.Impl(applicationContext)
        sendMailButton.setOnClickListener(this::sendMail)
    }

    // https://stackoverflow.com/questions/32954413/android-contact-picker-get-name-number-email
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val email_address = EMAIL_RETRIEVER.retrieve(uri)

                // https://developer.android.com/guide/components/intents-common#ComposeEmail
                val emailIntent = Intent(Intent.ACTION_SENDTO)

                emailIntent.data = Uri.parse("mailto:")
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email_address))

                // TODO: wynies napis do resources
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wiadomość z pracy domowej")

                val componentName2 = emailIntent.resolveActivity(packageManager)
                if (componentName2 != null) {
                    // jesli znaleziono, to owieramy
                    startActivity(emailIntent)
                }

            }

        }

    }

    companion object {
        lateinit var EMAIL_RETRIEVER: EmailRetriever
    }

    private fun sendMail(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Email.CONTENT_TYPE

        val componentName = intent.resolveActivity(packageManager)
        if (componentName != null) {
            // jesli znaleziono, to owieramy
            startActivityForResult(intent, 101)
        }
    }

}
