package com.example.nearyou.model.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHandler(context: Context): SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "NearYou"

        private const val TABLE_CONFIGURATION = "ConfigurationTable"
        private const val KEY_ID = "TAG"
        private const val KEY_value = "Value"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreateTable = ("CREATE TABLE " + TABLE_CONFIGURATION + "("
                + KEY_ID + " VARCHAR(50) PRIMARY KEY,"
                + KEY_value + " VARCHAR(50))")
        db?.execSQL(sqlCreateTable)

        // Add theme data
        val sqlTheme = "INSERT INTO $TABLE_CONFIGURATION VALUES ('theme', 'light')"
        db?.execSQL(sqlTheme)

        // Add language data
        val sqlLanguage = "INSERT INTO $TABLE_CONFIGURATION VALUES ('language', 'fr')"
        db?.execSQL(sqlLanguage)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONFIGURATION")
        onCreate(db)
    }


    fun getConf(key: String): String {
        var result = "aa"

        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CONFIGURATION WHERE $KEY_ID = '$key'"

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(KEY_value))
            }
        } catch (e: Exception) {

        }

        cursor?.close()

        return  result
    }

    fun changeConf(key: String, value: String) {
        val db = this.writableDatabase

        db.execSQL("UPDATE $TABLE_CONFIGURATION SET $KEY_value='$value' WHERE $KEY_ID='$key'")
    }
}