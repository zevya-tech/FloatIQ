package com.harish.floatiq.overlay

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.harish.floatiq.ui.currency.*
import com.harish.floatiq.ui.theme.ThemeManager
import com.harish.floatiq.ui.theme.ThemeType
import kotlinx.coroutines.*

object OverlayCurrencyConverterView {

    fun create(
        context: Context
    ): View {

        val currentTheme =
            ThemeManager.currentTheme.value

        val primaryColor: Int
        val tertiaryColor: Int

        when (currentTheme) {

            ThemeType.NEON_BLUE -> {
                primaryColor = 0xFF2563EB.toInt()
                tertiaryColor = 0xFF60A5FA.toInt()
            }

            ThemeType.EMERALD_GREEN -> {
                primaryColor = 0xFF059669.toInt()
                tertiaryColor = 0xFF34D399.toInt()
            }

            ThemeType.SUNSET_ORANGE -> {
                primaryColor = 0xFFEA580C.toInt()
                tertiaryColor = 0xFFFB923C.toInt()
            }

            ThemeType.MIDNIGHT_PURPLE -> {
                primaryColor = 0xFF7C3AED.toInt()
                tertiaryColor = 0xFFA78BFA.toInt()
            }

            ThemeType.RCB_CHAMPIONS -> {
                primaryColor = 0xFFEC1C24.toInt()
                tertiaryColor = 0xFFFFD700.toInt()
            }

        }

        val root =
            LinearLayout(context).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    30,
                    30,
                    30,
                    30
                )
            }

        val currencies =
            CurrencyData.currencies

        val amountInput =
            EditText(context).apply {

                hint = "Enter Amount"

                inputType =
                    InputType.TYPE_CLASS_NUMBER or
                            InputType.TYPE_NUMBER_FLAG_DECIMAL

                setTextColor(Color.WHITE)

                setHintTextColor(
                    Color.LTGRAY
                )

                background =
                    GradientDrawable().apply {

                        cornerRadius = 25f

                        setColor(
                            0xFF1E293B.toInt()
                        )

                        setStroke(
                            2,
                            tertiaryColor
                        )
                    }
            }

        val fromSpinner =
            Spinner(context)

        val toSpinner =
            Spinner(context)

        val adapter =
            object : ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                currencies
            ) {

                override fun getView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {

                    return super.getView(
                        position,
                        convertView,
                        parent
                    ).apply {

                        (this as TextView)
                            .setTextColor(
                                Color.WHITE
                            )
                    }
                }
            }

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        fromSpinner.adapter =
            adapter

        toSpinner.adapter =
            adapter

        fromSpinner.setSelection(0)
        toSpinner.setSelection(1)

        val resultText =
            TextView(context).apply {

                text = "Result"

                textSize = 22f

                gravity =
                    Gravity.CENTER

                setTextColor(
                    tertiaryColor
                )
            }

        val rateText =
            TextView(context).apply {

                gravity =
                    Gravity.CENTER

                setTextColor(
                    Color.WHITE
                )
            }

        val loading =
            ProgressBar(context).apply {

                visibility =
                    View.GONE
            }

        val convertButton =
            Button(context).apply {

                text = "Convert"

                setTextColor(
                    Color.WHITE
                )

                background =
                    GradientDrawable().apply {

                        cornerRadius = 25f

                        setColor(
                            primaryColor
                        )
                    }

                setOnClickListener {

                    val amountText =
                        amountInput.text.toString()

                    val amount =
                        amountText.toDoubleOrNull()

                    if (amount == null) {

                        Toast.makeText(
                            context,
                            "Input format is not valid",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@setOnClickListener
                    }

                    if (
                        fromSpinner.selectedItem.toString() ==
                        toSpinner.selectedItem.toString()
                    ) {

                        Toast.makeText(
                            context,
                            "Source and target currencies cannot be the same",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@setOnClickListener
                    }
                    loading.visibility =
                        View.VISIBLE

                    CoroutineScope(
                        Dispatchers.IO
                    ).launch {

                        try {

                            val response =
                                RetrofitInstance.api
                                    .getRate(
                                        fromSpinner
                                            .selectedItem
                                            .toString(),

                                        toSpinner
                                            .selectedItem
                                            .toString()
                                    )

                            if (
                                response.isSuccessful
                            ) {

                                val rate =
                                    response.body()
                                        ?.rates
                                        ?.values
                                        ?.firstOrNull()
                                        ?: 0.0

                                val converted =
                                    amount * rate

                                withContext(
                                    Dispatchers.Main
                                ) {

                                    loading.visibility =
                                        View.GONE

                                    rateText.text =
                                        "1 ${
                                            fromSpinner.selectedItem
                                        } = ${
                                            CurrencyConverterEngine
                                                .formatResult(rate)
                                        } ${
                                            toSpinner.selectedItem
                                        }"

                                    resultText.text =
                                        CurrencyConverterEngine
                                            .formatResult(
                                                converted
                                            ) +
                                                " ${
                                                    toSpinner.selectedItem
                                                }"
                                }
                            }

                       }
                        catch (
                            e: Exception
                        ) {

                            withContext(
                                Dispatchers.Main
                            ) {

                                loading.visibility =
                                    View.GONE

                                resultText.text =
                                    "Internet Required"
                            }
                        }
                    }
                }
            }

        val closeButton =
            Button(context).apply {

                text = "Close"

                setTextColor(
                    Color.WHITE
                )

                background =
                    GradientDrawable().apply {

                        cornerRadius = 25f

                        setColor(
                            0xFFDC2626.toInt()
                        )
                    }

                setOnClickListener {

                    FloatingService
                        .hideExpandedPanel()
                }
            }

        root.addView(amountInput)
        root.addView(fromSpinner)

        root.addView(
            Space(context).apply {
                minimumHeight = 50
            }
        )

        root.addView(toSpinner)

        root.addView(
            Space(context).apply {
                minimumHeight = 25
            }
        )

        root.addView(convertButton)
        root.addView(loading)
        root.addView(rateText)
        root.addView(resultText)
        root.addView(closeButton)

        return ScrollView(context).apply {

            addView(root)

            background =
                GradientDrawable().apply {

                    cornerRadius = 30f

                    setColor(
                        0xEE0F172A.toInt()
                    )

                    setStroke(
                        3,
                        tertiaryColor
                    )
                }
        }
    }
}