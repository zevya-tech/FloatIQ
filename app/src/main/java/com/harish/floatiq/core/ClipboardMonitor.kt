//package com.harish.floatiq.core

package com.harish.floatiq.core

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log

class ClipboardMonitor(
    private val context: Context
) {

    private val clipboardManager =
        context.getSystemService(
            Context.CLIPBOARD_SERVICE
        ) as ClipboardManager

    fun startMonitoring() {

        clipboardManager.addPrimaryClipChangedListener {

            val clipData: ClipData? =
                clipboardManager.primaryClip

            val copiedText =
                clipData
                    ?.getItemAt(0)
                    ?.text
                    ?.toString()

            if (!copiedText.isNullOrEmpty()) {

                Log.d(
                    "FLOATIQ_CLIPBOARD",
                    "Copied Text: $copiedText"
                )
            }
        }
    }
}