package com.example.sellmate.ui.product


import HistoryViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sellmate.data.model.History


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel) {
    val historyList by historyViewModel.historyList.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        historyViewModel.loadHistory() // Memuat data saat layar dibuka
    }

    LazyColumn {
        items(historyList) { history ->
            HistoryItem(history = history)
        }
    }
}

@Composable
fun HistoryItem(history: History) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Deskripsi: ${history.description}")
            Text("Waktu: ${formatTimestamp(history.timestamp)}")
        }
    }
}


