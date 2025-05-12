package dev.balikin.poject.storage

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<BalikinDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(BalikinDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}