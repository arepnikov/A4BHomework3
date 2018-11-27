package com.daftmobile.a4bhomework3

import android.content.Context
import android.support.v7.app.AlertDialog

fun showMsgDialog(context: Context, msg: CharSequence) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(msg)
    builder.setPositiveButton("OK") { _, _ -> }
    builder.show()
}
