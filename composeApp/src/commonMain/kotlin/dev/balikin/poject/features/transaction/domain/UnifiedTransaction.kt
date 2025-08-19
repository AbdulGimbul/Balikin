package dev.balikin.poject.features.transaction.domain

import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.data.TransactionType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Unified transaction model that represents both local and online transactions
 */
data class UnifiedTransaction(
    val id: String, // For local: Long as String, for online: composite key 
    val name: String,
    val email: String? = null, // Only available for online transactions
    val amount: Double,
    val type: TransactionType,
    val note: String,
    val date: LocalDateTime,
    val isPaid: Boolean = false,
    val isOnline: Boolean = false,
    val isUserOwed: Boolean = true, // True if money is owed TO the user, False if user owes money
    val onlinePartnerIsOnline: Boolean = false // Only for online transactions
) {
    companion object {

        /**
         * Convert local transaction entity to unified transaction
         */
        fun fromLocal(
            entity: TransactionEntity,
            currentUserEmail: String? = null
        ): UnifiedTransaction {
            return UnifiedTransaction(
                id = entity.id.toString(),
                name = entity.name,
                email = null,
                amount = entity.amount,
                type = entity.type,
                note = entity.note,
                date = entity.dueDate,
                isPaid = entity.isPaid,
                isOnline = false,
                isUserOwed = entity.type == TransactionType.Piutang, // Piutang = user is owed money
                onlinePartnerIsOnline = false
            )
        }

        /**
         * Convert online transaction to unified transaction
         */
        @OptIn(ExperimentalTime::class)
        fun fromOnline(
            onlineTransaction: OnlineTransaction,
            currentUserEmail: String
        ): UnifiedTransaction {
            // Determine if this is a debt (Utang) or receivable (Piutang) from current user's perspective
            val isUserReceivable = onlineTransaction.receivable.email == currentUserEmail
            val isUserPayable = onlineTransaction.payable.email == currentUserEmail

            // Determine the partner (the other person in the transaction)
            val partner = if (isUserReceivable) {
                onlineTransaction.payable
            } else {
                onlineTransaction.receivable
            }

            // Convert kategori to our TransactionType from user's perspective
            val transactionType = when {
                // If user is receivable and kategori is PIUTANG -> user is owed money (Piutang)
                isUserReceivable && onlineTransaction.kategori == "PIUTANG" -> TransactionType.Piutang
                // If user is payable and kategori is PIUTANG -> user owes money (Utang)  
                isUserPayable && onlineTransaction.kategori == "PIUTANG" -> TransactionType.Utang
                // If user is receivable and kategori is UTANG -> user owes money (Utang)
                isUserReceivable && onlineTransaction.kategori == "UTANG" -> TransactionType.Utang
                // If user is payable and kategori is UTANG -> user is owed money (Piutang)
                isUserPayable && onlineTransaction.kategori == "UTANG" -> TransactionType.Piutang
                else -> TransactionType.Piutang // Default fallback
            }

            // Parse date
            val transactionDate = try {
                // Convert ISO string to LocalDateTime
                kotlinx.datetime.Instant.parse(onlineTransaction.date)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            } catch (e: Exception) {
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }

            return UnifiedTransaction(
                id = "${partner.email}_${onlineTransaction.date}_${onlineTransaction.nominal}", // Composite key
                name = partner.name,
                email = partner.email,
                amount = onlineTransaction.nominal.toDoubleOrNull() ?: 0.0,
                type = transactionType,
                note = onlineTransaction.desc,
                date = transactionDate,
                isPaid = false, // Online transactions don't have paid status yet
                isOnline = true,
                isUserOwed = transactionType == TransactionType.Piutang,
                onlinePartnerIsOnline = partner.isOnline
            )
        }
    }
}
