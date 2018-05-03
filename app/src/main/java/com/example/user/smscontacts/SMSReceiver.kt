package com.example.user.smscontacts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.telephony.SmsMessage


/**
 * Created by user on 12/5/2017.
 */

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {

        var pdus = p1.extras.get("pdus") as Array<Any>
        var text = ""
        var phone = ""
        var result = ""
        for (x in 0 until pdus.size) {
            var msg = SmsMessage.createFromPdu(pdus[x] as ByteArray)
            text = msg.displayMessageBody
            phone = msg.originatingAddress
        }

        var password = text.substring(1, 5)
        var name = text.substring(text.lastIndexOf("#") + 1)

        if (password == "1234") {
            var search = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +
                    " like '%" + name + "%'"
            var cur = p0.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, search, null, null)
            cur.moveToFirst()
            while (!cur.isAfterLast) {
                result += "\n" + (cur.getString(cur.getColumnIndex
                (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) +
                        "\n" + cur.getString(cur.getColumnIndex
                (ContactsContract.CommonDataKinds.Phone.NUMBER)))
                cur.moveToNext()
            }
            var mr = SmsManager.getDefault()
            mr.sendTextMessage(phone, null, result,
                    null, null)
        }


    }
}
