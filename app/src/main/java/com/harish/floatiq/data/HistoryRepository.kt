package com.harish.floatiq.data

import kotlinx.coroutines.flow.Flow

class HistoryRepository(
    private val historyDao: HistoryDao
) {

    val allHistory: Flow<List<HistoryItem>> =
        historyDao.getAllHistory()

    suspend fun insertHistory(historyItem: HistoryItem) {
        historyDao.insertHistory(historyItem)
    }

    suspend fun deleteHistory(historyItem: HistoryItem) {
        historyDao.deleteHistory(historyItem)
    }

    suspend fun clearHistory() {
        historyDao.clearHistory()
    }
}