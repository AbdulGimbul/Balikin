package dev.balikin.poject.storage

import androidx.room.TypeConverter
import dev.balikin.poject.features.transaction.data.TransactionType
import kotlinx.datetime.LocalDateTime

class TransactionTypeConverters {

    @TypeConverter
    fun fromTransactionType(value: String?): TransactionType? {
        return value?.let { TransactionType.valueOf(it) }
    }

    @TypeConverter
    fun toTransactionTypeString(type: TransactionType?): String? {
        return type?.name
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String = dateTime.toString()

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime = LocalDateTime.parse(value)
}