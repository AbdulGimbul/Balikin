package dev.balikin.poject.storage

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<BalikinDatabase>
}