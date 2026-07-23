package com.harish.floatiq.overlay

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.graphics.drawable.GradientDrawable
import android.widget.LinearLayout
import android.widget.GridLayout
import com.harish.floatiq.ui.scientific.ScientificCalculatorEngine
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import com.harish.floatiq.ui.theme.ThemeManager
import com.harish.floatiq.ui.theme.ThemeType
import com.harish.floatiq.MainActivity
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.ImageView
import com.harish.floatiq.R
import androidx.core.widget.TextViewCompat
import android.util.TypedValue
import android.media.AudioAttributes
import android.media.SoundPool
class FloatingService : Service() {
    companion object {

        var instance: FloatingService? = null

fun hideExpandedPanel() {

    instance?.run {

        expandedPanel.animate()
            .scaleX(0.85f)
            .scaleY(0.85f)
            .alpha(0f)
            .setDuration(180)
            .withEndAction {

                expandedPanel.visibility =
                    View.GONE

                isExpanded = false
            }
            .start()
    }
}
    }

    private fun startBubblePulse() {

        floatingView.animate()
            .scaleX(1.04f)
            .scaleY(1.04f)
            .setDuration(1200)
            .withEndAction {

                floatingView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(1200)
                    .withEndAction {

                        startBubblePulse()
                    }
                    .start()
            }
            .start()
    }
    private fun vibratePhone() {
        Log.d("FLOATIQ", "VIBRATE CALLED")
        val vibrator =
            getSystemService(VIBRATOR_SERVICE)
                    as Vibrator

        if (android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.O
        ) {

            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    150,
                    255
                )
            )

        } else {

            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }
    private fun playClickSound() {

        if (clickSoundId != 0) {

            soundPool.play(
                clickSoundId,
                1f,
                1f,
                1,
                0,
                1f
            )
        }
    }
    private val radialButtons =
        mutableListOf<View>()
    private var bubbleX = 100
    private var bubbleY = 300
    private var radialMenuVisible = false
    private lateinit var calculatorView: ScrollView
    private var bubbleSize = 180
    private var screenHeight = 0
    private fun toggleRadialMenu() {

        if (radialMenuVisible) {

            hideRadialMenu()

        } else {

            showRadialMenu()
        }
    }
    private fun showRadialMenu() {

        radialMenuVisible = true
        vibratePhone()
        val screenWidth =
            resources.displayMetrics.widthPixels



        val showLeftSide =
            bubbleX > screenWidth / 2


        val spacing =
            (screenWidth * 0.12f)
                .toInt()
        val radialSize =
            (screenWidth * 0.10f)
                .toInt()
                .coerceIn(90, 140)
        radialItems.forEachIndexed { index, item ->


            val button = ImageView(this).apply {

                val iconRes =
                    when(item.action) {

                        OverlayAction.CALCULATOR ->
                            R.drawable.ic_calculate

                        OverlayAction.OCR ->
                            R.drawable.ic_document_scanner

                        OverlayAction.UNIT_CONVERTER ->
                            R.drawable.ic_straighten

                        OverlayAction.CURRENCY_CONVERTER ->
                            R.drawable.ic_currency_exchange

                        OverlayAction.CLOSE ->
                            R.drawable.ic_close
                    }

                setImageResource(iconRes)

                setColorFilter(
                    android.graphics.Color.WHITE
                )

                background =
                    GradientDrawable().apply {

                        shape = GradientDrawable.OVAL

                        setColor(
                            if (item.action == OverlayAction.CLOSE)
                                0xFFDC2626.toInt()
                            else
                                0xFF2563EB.toInt()
                        )

                        setStroke(
                            3,
                            0x33FFFFFF
                        )

                    }

                scaleType = ImageView.ScaleType.CENTER_INSIDE


                setPadding(
                    18,
                    18,
                    18,
                    18
                )
                elevation = 16f
            }

            val layoutParams =

                WindowManager.LayoutParams(
                    radialSize,
                    radialSize,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )

            layoutParams.gravity =
                Gravity.TOP or Gravity.START

            layoutParams.y =
                bubbleY + (index * spacing)

            layoutParams.x =
                if (showLeftSide) {

                    bubbleX - 140

                } else {

                    bubbleX + bubbleSize + 20
                }

            button.setOnClickListener {

                handleRadialAction(item.action)

                hideRadialMenu()
            }

            windowManager.addView(
                button,
                layoutParams
            )

// Start tiny
            button.scaleX = 0f
            button.scaleY = 0f

// Animate popup
            button.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200)
                .setStartDelay((index * 40).toLong())
                .start()

            radialButtons.add(button)
        }
    }

    private fun hideRadialMenu() {

        radialButtons.forEach {

            try {

                windowManager.removeView(it)

            } catch (_: Exception) {
            }
        }

        radialButtons.clear()

        radialMenuVisible = false
    }

private fun handleRadialAction(
    action: OverlayAction
){

    when(action) {

        OverlayAction.CALCULATOR -> {

            currentScreen =
                OverlayScreen.CALCULATOR


            if (!isExpanded) {

                toggleExpandedState()

            } else {

                currentScreen =
                    OverlayScreen.CALCULATOR

                expandedPanel.removeAllViews()

                expandedPanel.addView(
                    calculatorView
                )
            }
        }



        OverlayAction.OCR -> {

//            val intent =
//                Intent(
//                    this,
//                    MainActivity::class.java
//                )
//
//            intent.putExtra(
//                "open_screen",
//                "ocr"
//            )
//
//            intent.addFlags(
//                Intent.FLAG_ACTIVITY_NEW_TASK
//            )
//
//            startActivity(intent)
            val intent = Intent(this, MainActivity::class.java).apply {

                putExtra("open_screen", "ocr")

                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            }

            startActivity(intent)
        }

        OverlayAction.UNIT_CONVERTER -> {

            currentScreen =
                OverlayScreen.UNIT_CONVERTER

            expandedPanel.removeAllViews()

            expandedPanel.addView(
                OverlayPanelFactory
                    .createUnitConverterPanel(this)
            )

            toggleExpandedState()
        }

        OverlayAction.CURRENCY_CONVERTER -> {

            currentScreen =
                OverlayScreen.CURRENCY_CONVERTER

            expandedPanel.removeAllViews()

            expandedPanel.addView(
                OverlayPanelFactory
                    .createCurrencyPanel(this)
            )

            toggleExpandedState()
        }

        OverlayAction.CLOSE -> {
            vibratePhone()
            hideRadialMenu()

            expandedPanel.visibility = View.GONE

            floatingView.animate().cancel()

            floatingView.animate()
                .scaleX(1.4f)
                .scaleY(1.4f)
                .setDuration(120)
                .withEndAction {

                    floatingView.animate()
                        .scaleX(0f)
                        .scaleY(0f)
                        .alpha(0f)
                        .rotation(360f)
                        .setDuration(220)
                        .withEndAction {

                            stopSelf()
                        }
                        .start()
                }
                .start()
        }
    }
}

private val radialItems = listOf(

    OverlayMenuItem(
        OverlayAction.CALCULATOR,
        "Calculator",
        ""
    ),

    OverlayMenuItem(
        OverlayAction.OCR,
        "OCR",
        ""
    ),

    OverlayMenuItem(
        OverlayAction.UNIT_CONVERTER,
        "Units",
        ""
    ),

    OverlayMenuItem(
        OverlayAction.CURRENCY_CONVERTER,
        "Currency",
        ""
    ),

    OverlayMenuItem(
        OverlayAction.CLOSE,
        "Close",
        ""
    )
)
    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: FrameLayout
    private lateinit var expandedPanel: FrameLayout
    private lateinit var panelParams: WindowManager.LayoutParams
    private var isExpanded = false
    private var expression = ""
    private var scientificExpanded = false
    private lateinit var soundPool: SoundPool

    private var clickSoundId = 0
    private var currentScreen =
        OverlayScreen.CALCULATOR
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        soundPool =
            SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .build()

        clickSoundId =
            soundPool.load(
                this,
                R.raw.click12,
                1
            )
        startForeground(
            1001,
            OverlayNotificationHelper
                .createNotification(this)
        )
        screenHeight =
            resources.displayMetrics.heightPixels
        instance = this
        windowManager =
            getSystemService(WINDOW_SERVICE) as WindowManager
        val prefs =
            getSharedPreferences(
                "floatiq_settings",
                MODE_PRIVATE
            )

        val savedOverlay =
            prefs.getString(
                "default_overlay",
                "Calculator"
            )

        currentScreen =
            when(savedOverlay) {

                "Unit Converter" ->
                    OverlayScreen.UNIT_CONVERTER

                "Currency Converter" ->
                    OverlayScreen.CURRENCY_CONVERTER

                else ->
                    OverlayScreen.CALCULATOR
            }
        val displayMetrics = resources.displayMetrics

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
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


        }

        floatingView = FrameLayout(this)

        floatingView.setOnClickListener { }

        expandedPanel = FrameLayout(this).apply {

            visibility = View.GONE

            background = GradientDrawable().apply {

                cornerRadius = 40f


                setColor(0xEE0F172A.toInt())

                setStroke(
                    4,
                    tertiaryColor
                )
            }


            elevation = 20f
        }

        calculatorView = ScrollView(this)

        val calculatorLayout =
            LinearLayout(this).apply {

                orientation = LinearLayout.VERTICAL

                setPadding(30, 30, 30, 30)

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

        val displayScroll = HorizontalScrollView(this).apply {

            isHorizontalScrollBarEnabled = false

            isFillViewport = true

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }


        val displayText = TextView(this).apply {

            text = "0"


            textSize = 36f

            gravity = Gravity.END or Gravity.CENTER_VERTICAL

            textAlignment = View.TEXT_ALIGNMENT_VIEW_END

            isSingleLine = true

            setHorizontallyScrolling(true)

            setTextColor(
                android.graphics.Color.WHITE
            )

            setPadding(20, 20, 20, 50)
        }

        displayScroll.addView(displayText)
        val scientificHeader = TextView(this).apply {

            text = "▼ Scientific Functions"

            textSize = 16f

            setTextColor(android.graphics.Color.WHITE)

            setPadding(15,20,15,20)

            background = GradientDrawable().apply {

                cornerRadius = 20f

                setColor(0xFF1E293B.toInt())
            }
        }
        val scientificGrid = GridLayout(this).apply {

            rowCount = 3
            columnCount = 4
        }

        val normalGrid = GridLayout(this).apply {

            rowCount = 5
            columnCount = 4
        }
        scientificGrid.visibility = View.GONE
        scientificHeader.setOnClickListener {

            scientificExpanded = !scientificExpanded

            if(scientificExpanded){

                scientificGrid.visibility = View.VISIBLE

                scientificHeader.text =
                    "▲ Scientific Functions"

            }else{

                scientificGrid.visibility = View.GONE

                scientificHeader.text =
                    "▼ Scientific Functions"
            }
        }

        val buttons = listOf(

            "sin", "cos", "tan", "log",
            "ln", "√", "^2", "π",
            "e", "(", ")", "%",

            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "×",
            "C", "⌫", "0", "/",

            "="
        )

        buttons.forEach { buttonText ->


            val button = TextView(this).apply {

                text = buttonText

                val buttonTextSize =
                    (screenWidth * 0.018f)
                        .coerceIn(16f, 24f)

                textSize = buttonTextSize
                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                    this,
                    12,
                    24,
                    1,
                    TypedValue.COMPLEX_UNIT_SP
                )
                gravity = Gravity.CENTER

                setTextColor(
                    android.graphics.Color.WHITE
                )

                val buttonColor =
                    when (buttonText) {

                        "=" -> 0xFF00C853.toInt()

                        "C" -> 0xFFDC2626.toInt()

                        "sin", "cos", "tan",
                        "log", "ln", "√",
                        "^2", "π", "e",
                        "(", ")" -> tertiaryColor

                        "+", "-", "×", "/", "%" ->
                            primaryColor

                        else -> {

                            when (currentTheme) {

                                ThemeType.RCB_CHAMPIONS ->
                                    0xFF1A1A1A.toInt()


                                else ->
                                    0xFF1E293B.toInt()
                            }
                        }
                    }

                background = GradientDrawable().apply {

                    cornerRadius = 40f

                    setColor(buttonColor)

                    setStroke(
                        2,
                        0x22FFFFFF
                    )
                }

                setPadding(30, 30, 30, 30)
            }


                val params = GridLayout.LayoutParams().apply {

                    val buttonSize =
                        (screenWidth * 0.12f)
                            .toInt()
                            .coerceIn(90, 140)

                    val scientificButton =
                        buttonText.length > 1

                    width =
                        if (scientificButton)
                            (buttonSize * 1.25f).toInt()
                        else
                            buttonSize

                    height =
                        (buttonSize * 1.15f).toInt()

                    setMargins(8, 8, 8, 8)
                }
            if (buttonText == "=") {



                params.rowSpec =
                    GridLayout.spec(4)
                params.columnSpec =
                    GridLayout.spec(0, 4)

                params.width =
                    GridLayout.LayoutParams.MATCH_PARENT
            }
            button.layoutParams = params

            button.setOnClickListener {
                playClickSound()
                    when (buttonText) {

                        "C" -> {

                            expression = ""

                            displayText.text = "0"
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }
                        "⌫" -> {

                            if (expression.isNotEmpty()) {

                                expression = expression.dropLast(1)

                                displayText.text =
                                    if (expression.isEmpty())
                                        "0"
                                    else
                                        expression
                                displayScroll.post {
                                    displayScroll.fullScroll(View.FOCUS_RIGHT)
                                }
                            }
                        }

                        "=" -> {

                            try {
                                val cleanExpression = expression.replace(",", "")
//                                expression =
//                                    ScientificCalculatorEngine
//                                        .evaluate(expression)
                                expression =
                                    ScientificCalculatorEngine
                                        .evaluate(cleanExpression)
                                displayText.text = expression
                                displayScroll.post {
                                    displayScroll.fullScroll(View.FOCUS_RIGHT)
                                }

                            } catch (e: Exception) {

                                expression = ""
                                displayText.text = "Error"
                            }
                        }

                        "sin" -> {
                            expression = expression.replace(",", "")
                            expression += "sin("
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }

                        "cos" -> {
                            expression = expression.replace(",", "")
                            expression += "cos("
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }

                        "tan" -> {
                            expression = expression.replace(",", "")
                            expression += "tan("
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }

                        "log" -> {
                            expression = expression.replace(",", "")
                            expression += "log("
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }

                        }

                        "ln" -> {
                            expression = expression.replace(",", "")
                            expression += "ln("
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }

                        "√" -> {
                            expression = expression.replace(",", "")
                            expression += "√("
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }


                        "^2" -> {

                            if (expression.isNotEmpty()) {
                                expression = expression.replace(",", "")
                                expression =
                                    "($expression)^2"

                                displayText.text = expression

                                displayScroll.post {
                                    displayScroll.fullScroll(View.FOCUS_RIGHT)
                                }
                            }
                        }
                        "π" -> {
                            expression = expression.replace(",", "")
                            expression += "π"
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }

                        "e" -> {
                            expression = expression.replace(",", "")
                            expression += "e"
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }

                        "(" -> {
                            expression = expression.replace(",", "")
                            expression += "("
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }

                        ")" -> {
                            expression = expression.replace(",", "")
                            expression += ")"
                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }
                        "%" -> {
                            expression = expression.replace(",", "")
                            expression += "%"

                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }
                        else -> {
                            expression = expression.replace(",", "")
                            expression += buttonText

                            displayText.text = expression
                            displayScroll.post {
                                displayScroll.fullScroll(View.FOCUS_RIGHT)
                            }
                        }
                    }
                }

//            buttonGrid.addView(button)
            when(buttonText){

                "sin","cos","tan","log",
                "ln","√","^2","π",
                "e","(",")","%" -> {

                    scientificGrid.addView(button)
                }

                else -> {

                    normalGrid.addView(button)
                }
            }
        }
        bubbleSize =
            (screenWidth * 0.15f)
                .toInt()
                .coerceIn(120, 220)
        val textView = TextView(this).apply {

            text = "FI"

            textSize = 22f

            gravity = Gravity.CENTER

            setTextColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.white
                )
            )

            val bubbleDrawable = GradientDrawable().apply {

                shape = GradientDrawable.OVAL


                colors = intArrayOf(
                    secondaryColor,
                    primaryColor,
                    tertiaryColor
                )
                gradientType = GradientDrawable.LINEAR_GRADIENT


                setStroke(
                    8,
                    0xFFFFFFFF.toInt()
                )
            }

            background = bubbleDrawable


            elevation = 30f

            layoutParams = FrameLayout.LayoutParams(
                bubbleSize,
                bubbleSize
            )
        }
        textView.translationZ = 30f
        floatingView.addView(textView)

        val closeButton = TextView(this).apply {

            text = "Close"

            textSize = 18f

            gravity = Gravity.CENTER

            setTextColor(android.graphics.Color.WHITE)

            setPadding(40, 25, 40, 25)

            setBackgroundColor(
                0xFFDC2626.toInt()
            )

            setOnClickListener {

                hideExpandedPanel()
            }

        }

//        calculatorLayout.addView(displayScroll)
//        calculatorLayout.addView(buttonGrid)
//
//        calculatorLayout.addView(closeButton)
        calculatorLayout.addView(displayScroll)

        calculatorLayout.addView(scientificHeader)

        calculatorLayout.addView(scientificGrid)

        calculatorLayout.addView(normalGrid)

        calculatorLayout.addView(closeButton)
        calculatorView.addView(calculatorLayout)
        expandedPanel.addView(calculatorView)
        val params =

            WindowManager.LayoutParams(
                bubbleSize,
                bubbleSize,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

        params.gravity = Gravity.TOP or Gravity.START

        params.x = 100
        params.y = 300
        bubbleX = params.x
        bubbleY = params.y



        val panelWidth =
            (screenWidth * 0.75f)
                .toInt()
                .coerceIn(650, 1100)


        val panelHeight =
            (screenHeight * 0.45f)
                .toInt()
                .coerceIn(500, 1000)
        panelParams =
            WindowManager.LayoutParams(

                panelWidth,
                panelHeight,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            )
        panelParams.gravity =
            Gravity.TOP or Gravity.START

        panelParams.x = 320
        panelParams.y = 250


        windowManager.addView(
            expandedPanel,
            panelParams
        )
        windowManager.addView(floatingView, params)
        startBubblePulse()
        floatingView.setOnTouchListener(
            object : View.OnTouchListener {

                private var initialX = 0
                private var initialY = 0

                private var initialTouchX = 0f
                private var initialTouchY = 0f
                private var downTime = 0L
                private var isDragging = false

                override fun onTouch(
                    v: View?,
                    event: MotionEvent
                ): Boolean {

                    when (event.action) {

                        MotionEvent.ACTION_DOWN -> {
                            downTime = System.currentTimeMillis()
                            initialX = params.x
                            initialY = params.y

                            initialTouchX = event.rawX
                            initialTouchY = event.rawY

                            isDragging = false

                            return true
                        }

                        MotionEvent.ACTION_MOVE -> {

                            val dx =
                                (event.rawX - initialTouchX).toInt()

                            val dy =
                                (event.rawY - initialTouchY).toInt()

                            if (kotlin.math.abs(dx) > 10 ||
                                kotlin.math.abs(dy) > 10
                            ) {
                                hideRadialMenu()
                                isDragging = true
                            }



                            params.x = initialX + dx
                            params.y = initialY + dy
                            bubbleX = params.x
                            bubbleY = params.y
                            if (isExpanded) {
                                val panelWidth =
                                    panelParams.width


                                val openOnLeft =
                                    params.x + 220 + panelWidth > screenWidth

                                panelParams.x =
                                    if (openOnLeft) {

                                        params.x - panelWidth

                                    } else {

                                        params.x + 220
                                        params.x + bubbleSize + 20
                                    }

                                panelParams.y =
                                    params.y.coerceAtMost(
                                        screenHeight - panelParams.height - 50
                                    )
                                windowManager.updateViewLayout(
                                    expandedPanel,
                                    panelParams
                                )
                            }
                            windowManager.updateViewLayout(
                                floatingView,
                                params
                            )

                            return true
                        }



                        MotionEvent.ACTION_UP -> {

                            v?.performClick()
                            if (!isDragging) {

                                val pressDuration =
                                    System.currentTimeMillis() - downTime

                                if (pressDuration > 600) {

                                    toggleRadialMenu()

                                } else {
                                    hideRadialMenu()
                                    floatingView.animate()
                                        .scaleX(0.9f)
                                        .scaleY(0.9f)
                                        .setDuration(80)
                                        .withEndAction {

                                            floatingView.animate()
                                                .scaleX(1f)
                                                .scaleY(1f)
                                                .setDuration(80)
                                                .start()
                                        }
                                        .start()
                                    val prefs =
                                        getSharedPreferences(
                                            "floatiq_settings",
                                            MODE_PRIVATE
                                        )

                                    val savedOverlay =
                                        prefs.getString(
                                            "default_overlay",
                                            "Calculator"
                                        )

                                    currentScreen =
                                        when(savedOverlay) {

                                            "Unit Converter" ->
                                                OverlayScreen.UNIT_CONVERTER

                                            "Currency Converter" ->
                                                OverlayScreen.CURRENCY_CONVERTER

                                            else ->
                                                OverlayScreen.CALCULATOR
                                        }

                                    toggleExpandedState()
                                }
                            }
                            else {
                                hideRadialMenu()
                                val bubbleWidth =
                                    params.width

                                val centerX =
                                    params.x + bubbleWidth / 2

                                params.x =
                                    if (centerX < screenWidth / 2) {

                                        0

                                    } else {

                                        screenWidth - bubbleWidth
                                    }
                                bubbleX = params.x
                                bubbleY = params.y
                                windowManager.updateViewLayout(
                                    floatingView,
                                    params
                                )

                                if (isExpanded) {
                                    val panelWidth =
                                        panelParams.width

                                    val openOnLeft =
                                        params.x + bubbleSize + 20 + panelWidth > screenWidth
                                    panelParams.x =
                                        if (openOnLeft) {

                                            params.x - panelWidth

                                        } else {
                                            params.x + bubbleWidth + 20
                                        }

                                    windowManager.updateViewLayout(
                                        expandedPanel,
                                        panelParams
                                    )
                                }
                            }

                            return true
                        }
                    }

                    return false
                }
            }
        )

    }




private fun toggleExpandedState() {

    hideRadialMenu()

    if (!isExpanded) {


        expandedPanel.removeAllViews()

        when(currentScreen) {

            OverlayScreen.CALCULATOR -> {

                expandedPanel.addView(
                    calculatorView
                )
            }


            OverlayScreen.UNIT_CONVERTER -> {

                expandedPanel.addView(
                    OverlayPanelFactory
                        .createUnitConverterPanel(this)
                )
            }

            OverlayScreen.CURRENCY_CONVERTER -> {

                expandedPanel.addView(
                    OverlayPanelFactory
                        .createCurrencyPanel(this)
                )
            }
        }
        val screenWidth =
            resources.displayMetrics.widthPixels

        val openOnLeft =
            bubbleX + bubbleSize + 20 + panelParams.width > screenWidth

        panelParams.x =
            if (openOnLeft) {

                bubbleX - panelParams.width

            } else {

                bubbleX + bubbleSize + 20
            }

        panelParams.y =
            bubbleY.coerceAtMost(
                screenHeight - panelParams.height - 50
            )

        windowManager.updateViewLayout(
            expandedPanel,
            panelParams
        )

        expandedPanel.visibility =
            View.VISIBLE
        expandedPanel.scaleX = 0.85f
        expandedPanel.scaleY = 0.85f
        expandedPanel.alpha = 0f

        expandedPanel.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(250)
            .start()
        isExpanded = true

    } else {


        expandedPanel.animate()
            .scaleX(0.85f)
            .scaleY(0.85f)
            .alpha(0f)
            .setDuration(180)
            .withEndAction {

                expandedPanel.visibility =
                    View.GONE
            }
            .start()
        isExpanded = false
    }
}
    override fun onDestroy() {
        instance = null
        soundPool.release()
        super.onDestroy()

        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }

        if (::expandedPanel.isInitialized) {
            windowManager.removeView(expandedPanel)
        }
    }
}
