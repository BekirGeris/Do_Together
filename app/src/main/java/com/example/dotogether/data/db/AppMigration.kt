package com.example.dotogether.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

abstract class AppMigration(startVersion: Int, endVersion: Int) : Migration(startVersion, endVersion) {

    fun execSQL(
        db: SupportSQLiteDatabase,
        tableName: String?,
        columnName: String?,
        sql: String?
    ) {
        if (!isColumnExists(db, tableName!!, columnName!!)) {
            db.execSQL(sql)
        }
    }

    private fun isColumnExists(
        db: SupportSQLiteDatabase,
        tableName: String,
        columnName: String
    ): Boolean {
        var isExists = false
        db.query("SELECT * FROM $tableName LIMIT 0", null).use { cursor ->
            isExists = cursor != null && cursor.getColumnIndex(columnName) != -1
        }
        return isExists
    }
}