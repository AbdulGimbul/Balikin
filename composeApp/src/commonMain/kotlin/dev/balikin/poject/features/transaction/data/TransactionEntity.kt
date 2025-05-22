package dev.balikin.poject.features.transaction.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val dueDate: LocalDateTime,
    val note: String,
    val amount: Double,
    val type: TransactionType,
    val isPaid: Boolean = false,
    val paidAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime? = null
)

enum class TransactionType {
    Utang, Piutang
}

//data class Transaction(
//    val name: String,
//    val date: String,
//    val note: String,
//    val amount: String,
//    val type: String
//)
//
//val dummyTransactions = listOf(
//    Transaction(
//        name = "Ilham Ardiansyah",
//        date = "17 Agustus ● 12.22 PM",
//        note = "bekas bell baras Ng",
//        amount = "Rp. 459.000",
//        type = "Utang"
//    ),
//    Transaction(
//        name = "Irsan Ramadhan",
//        date = "17 Agustus ● 12.22 PM",
//        note = "bekas bell baras Ng",
//        amount = "Rp. 99.000",
//        type = "Piutang"
//    ),
//    Transaction(
//        name = "Winggar Waharjut",
//        date = "17 Agustus ● 12.22 PM",
//        note = "bekas bell baras Ng",
//        amount = "Rp. 43.000",
//        type = "Piutang"
//    )
//)