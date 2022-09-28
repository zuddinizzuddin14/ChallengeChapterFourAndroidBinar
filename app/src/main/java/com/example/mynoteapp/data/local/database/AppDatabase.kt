package com.example.mynoteapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynoteapp.data.local.database.dao.NoteDao
import com.example.mynoteapp.data.local.database.entity.NoteEntity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory


@Database(entities = [NoteEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        private const val DB_NAME = "myNote.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val passphrase: ByteArray =
                    SQLiteDatabase.getBytes("myNote-hashed".toCharArray())
                val factory = SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .openHelperFactory(factory)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}