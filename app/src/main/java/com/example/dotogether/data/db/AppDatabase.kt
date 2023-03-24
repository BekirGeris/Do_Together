package com.example.dotogether.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dotogether.BuildConfig
import com.example.dotogether.data.dao.BasketDao
import com.example.dotogether.data.dao.UserDao
import com.example.dotogether.model.Basket
import com.example.dotogether.model.User

@Database(entities = [User::class, Basket::class], version = BuildConfig.ROOM_DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun basketDao(): BasketDao

    companion object {
        fun getMigrations(): Array<AppMigration> {
            return arrayOf(
                object : AppMigration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE Basket ADD COLUMN viewType INTEGER DEFAULT null")
                        database.execSQL("ALTER TABLE Basket ADD COLUMN viewId INTEGER DEFAULT null")
                    }
                }
            )
        }
    }
}