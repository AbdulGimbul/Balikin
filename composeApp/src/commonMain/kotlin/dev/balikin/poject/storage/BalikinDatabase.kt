package dev.balikin.poject.storage

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import dev.balikin.poject.features.history.data.HistoryDao
import dev.balikin.poject.features.transaction.data.TransactionDao
import dev.balikin.poject.features.transaction.data.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 1
)
@TypeConverters(TransactionTypeConverters::class)
@ConstructedBy(BalikinDatabaseConstructor::class)
abstract class BalikinDatabase : RoomDatabase() {
    abstract val transactionDao: TransactionDao
    abstract val historyDao: HistoryDao

    companion object {
        const val DB_NAME = "balikin.db"
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BalikinDatabaseConstructor : RoomDatabaseConstructor<BalikinDatabase> {
    override fun initialize(): BalikinDatabase
}