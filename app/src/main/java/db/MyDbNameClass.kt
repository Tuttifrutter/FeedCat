package db

import android.provider.BaseColumns

object MyDbNameClass:BaseColumns {
    const val TABLE_NAME = "records"
    const val COLUMN_NAME_USER = "username"
    const val COLUMN_NAME_SCORE = "score"
    const val COLUMN_NAME_DATE = "date"
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "records.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ("+
                            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                            "$COLUMN_NAME_USER TEXT, " +
                            "$COLUMN_NAME_SCORE INTEGER, " +
                                "$COLUMN_NAME_DATE TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${TABLE_NAME}"

    const val TABLE_NAME2 = "users"
    const val COLUMN_NAME_USERNAME = "user"
    const val CREATE_TABLE2 = "CREATE TABLE IF NOT EXISTS $TABLE_NAME2 ("+
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_NAME_USERNAME TEXT)"
    const val SQL_DELETE_TABLE2 = "DROP TABLE IF EXISTS ${TABLE_NAME2}"

}