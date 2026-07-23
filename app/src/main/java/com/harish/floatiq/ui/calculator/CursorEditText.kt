package com.harish.floatiq.ui.calculator

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CursorEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(
    context,
    attrs
) {


var onSelectionChangedCallback:
        ((Int, Int) -> Unit)? = null

    override fun onSelectionChanged(
        selStart: Int,
        selEnd: Int
    ) {

        super.onSelectionChanged(
            selStart,
            selEnd
        )


        if (selStart >= 0) {

            onSelectionChangedCallback?.invoke(
                selStart,
                selEnd
            )
        }
    }
}