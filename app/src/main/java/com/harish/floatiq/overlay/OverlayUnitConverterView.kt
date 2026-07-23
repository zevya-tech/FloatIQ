package com.harish.floatiq.overlay

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.*
import com.harish.floatiq.ui.converter.UnitConverterEngine
import com.harish.floatiq.overlay.FloatingService
import com.harish.floatiq.ui.theme.ThemeManager
import com.harish.floatiq.ui.theme.ThemeType
import android.view.ViewGroup
object OverlayUnitConverterView {

    fun create(
        context: Context
    ): View {
        val currentTheme =
            ThemeManager.currentTheme.value

        val primaryColor: Int
        val secondaryColor: Int
        val tertiaryColor: Int

        when (currentTheme) {

            ThemeType.NEON_BLUE -> {

                primaryColor = 0xFF2563EB.toInt()
                secondaryColor = 0xFF3B82F6.toInt()
                tertiaryColor = 0xFF60A5FA.toInt()
            }

            ThemeType.EMERALD_GREEN -> {

                primaryColor = 0xFF059669.toInt()
                secondaryColor = 0xFF10B981.toInt()
                tertiaryColor = 0xFF34D399.toInt()
            }

            ThemeType.SUNSET_ORANGE -> {

                primaryColor = 0xFFEA580C.toInt()
                secondaryColor = 0xFFF97316.toInt()
                tertiaryColor = 0xFFFB923C.toInt()
            }

            ThemeType.MIDNIGHT_PURPLE -> {

                primaryColor = 0xFF7C3AED.toInt()
                secondaryColor = 0xFF8B5CF6.toInt()
                tertiaryColor = 0xFFA78BFA.toInt()
            }

            ThemeType.RCB_CHAMPIONS -> {

                primaryColor = 0xFFEC1C24.toInt()
                secondaryColor = 0xFFB71C1C.toInt()
                tertiaryColor = 0xFFFFD700.toInt()
            }

//            ThemeType.ONE_PIECE -> {
//
//                primaryColor = 0xFFD32F2F.toInt()
//                secondaryColor = 0xFFFBC02D.toInt()
//                tertiaryColor = 0xFFFFFFFF.toInt()
//            }
        }
        val rootLayout =
            LinearLayout(context).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    30,
                    30,
                    30,
                    30
                )
                setBackgroundColor(
                    Color.TRANSPARENT
                )
            }

        val categories =
            listOf(
                "Length",
                "Weight",
                "Temperature",
                "Area",
                "Volume",
                "Speed",
                "Time",
                "Data Storage"
            )

        var selectedCategory =
            categories.first()

        val categorySpinner =
            Spinner(context)
        categorySpinner.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {

                bottomMargin = 20
            }
        categorySpinner.adapter =
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                categories
            )

        val valueInput =
            EditText(context).apply {

                hint = "Enter value"

                inputType =
                    InputType.TYPE_CLASS_NUMBER or
                            InputType.TYPE_NUMBER_FLAG_DECIMAL
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()

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
                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {

                        bottomMargin = 20
                    }
            }


        val fromSpinner =
            Spinner(context).apply {

                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {

                        topMargin = 20
                        bottomMargin = 20
                    }
            }

        val toSpinner =
            Spinner(context).apply {

                layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {

                        topMargin = 20
                        bottomMargin = 30
                    }
            }


fun updateUnits() {

    val units =
        UnitConverterEngine.getUnits(
            selectedCategory
        )

    val adapter =
        object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            units
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
                        .setTextColor(Color.WHITE)
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
}
        updateUnits()

        categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {


override fun onItemSelected(
    parent: AdapterView<*>?,
    view: View?,
    position: Int,
    id: Long
) {

    (view as? TextView)?.setTextColor(
        Color.WHITE
    )

    selectedCategory =
        categories[position]

    updateUnits()
}

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }

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

        val convertButton =

            Button(context).apply {
                background =
                    GradientDrawable().apply {

                        cornerRadius = 25f

                        setColor(primaryColor)
                    }

                setTextColor(Color.WHITE)
                text = "Convert"

                setOnClickListener {

                    try {

                        val value =
                            valueInput.text
                                .toString()
                                .toDouble()

                        val from =
                            fromSpinner
                                .selectedItem
                                .toString()

                        val to =
                            toSpinner
                                .selectedItem
                                .toString()

                        val result =
                            UnitConverterEngine.convert(
                                selectedCategory,
                                value,
                                from,
                                to
                            )

                        resultText.text =
                            UnitConverterEngine
                                .formatResult(
                                    result
                                )

                    } catch (e: Exception) {

                        resultText.text =
                            "Invalid Input"
                    }
                }
            }
        val closeButton =
            Button(context).apply {
                background =
                    GradientDrawable().apply {

                        cornerRadius = 25f

                        setColor(
                            0xFFDC2626.toInt()
                        )
                    }

                setTextColor(Color.WHITE)
                text = "Close"

                setOnClickListener {

                    FloatingService.hideExpandedPanel()
                }
            }
        rootLayout.addView(
            categorySpinner
        )

        rootLayout.addView(
            valueInput
        )

        rootLayout.addView(
            fromSpinner
        )

        rootLayout.addView(
            toSpinner
        )

        rootLayout.addView(
            convertButton
        )

        rootLayout.addView(
            resultText
        )
        rootLayout.addView(
            closeButton
        )
        return ScrollView(context).apply {

            addView(rootLayout)

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