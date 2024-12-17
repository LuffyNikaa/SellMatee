import androidx.lifecycle.ViewModel
import com.example.sellmate.data.model.History
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _historyList = MutableStateFlow<List<History>>(emptyList())
    val historyList: StateFlow<List<History>> = _historyList

    // Fungsi untuk menambahkan data history ke Firestore
    fun addHistory(description: String) {
        val history = History(
            id = db.collection("history").document().id,
            description = description
        )

        db.collection("history")
            .document(history.id)
            .set(history)
            .addOnSuccessListener {
                loadHistory() // Memperbarui daftar history setelah berhasil menambahkan
            }
            .addOnFailureListener {
                // Handle error
            }
    }

    // Fungsi untuk memuat data history dari Firestore
    fun loadHistory() {
        db.collection("history")
            .orderBy("timestamp")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val historyList = value?.documents?.mapNotNull { it.toObject(History::class.java) } ?: emptyList()
                _historyList.value = historyList
            }
    }
}
