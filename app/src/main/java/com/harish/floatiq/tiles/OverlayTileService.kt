package com.harish.floatiq.tiles

import android.content.Intent
import android.service.quicksettings.TileService
import androidx.core.content.ContextCompat
import com.harish.floatiq.overlay.FloatingService

class OverlayTileService : TileService() {

    override fun onClick() {

        super.onClick()
        qsTile?.apply {

            label = "FloatIQ Overlay"

            updateTile()
        }
        val intent =
            Intent(
                this,
                FloatingService::class.java
            )

        ContextCompat.startForegroundService(
            this,
            intent
        )
    }
}