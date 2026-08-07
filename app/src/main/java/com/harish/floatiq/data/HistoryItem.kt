//package com.harish.floatiq.data
package com.harish.floatiq.data
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "calc_history")
data class HistoryItem(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val expression: String,

    val result: String,

    val timestamp: Long
)
