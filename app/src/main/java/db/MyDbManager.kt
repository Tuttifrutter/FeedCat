package db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import db.MyDbNameClass.COLUMN_NAME_DATE
import db.MyDbNameClass.COLUMN_NAME_SCORE
import db.MyDbNameClass.COLUMN_NAME_USER
import db.MyDbNameClass.COLUMN_NAME_USERNAME
import db.MyDbNameClass.TABLE_NAME
import db.MyDbNameClass.TABLE_NAME2
import java.util.*
import kotlin.collections.ArrayList

class MyDbManager(context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase
    }

    fun insertToDbIfHigher(username:String, score: Int, date: String)
    {
        val List = readDbData();
        if(List.count()<15) {
            insertToDb(username, score, date)
            return
        }
        for(item in List)
        {
            if (Integer.parseInt(item.score) < score) {
                    db?.execSQL(
                        " DELETE FROM $TABLE_NAME" +
                                " WHERE $COLUMN_NAME_SCORE  = (SELECT MIN($COLUMN_NAME_SCORE) FROM $TABLE_NAME LIMIT 1)"
                    )
                    insertToDb(username, score, date)
                    break
            }

        }
    }

    fun insertToDb(username:String, score: Int, date: String)
    {
        val values = ContentValues().apply{
            put(COLUMN_NAME_USER, username)
            put(COLUMN_NAME_SCORE, score)
            put(COLUMN_NAME_DATE, date)
        }
        db?.insert(TABLE_NAME, null, values)
    }

    fun insertToUsersDb(username: String)
    {
        val values = ContentValues().apply{
            put(COLUMN_NAME_USERNAME, username)
        }
        db?.insert(TABLE_NAME2, null, values)
    }

    @SuppressLint("Range")
    fun readDbData() :ArrayList<MyDbString>{
        val dataList = ArrayList<MyDbString>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null,null,null,null,null,COLUMN_NAME_SCORE + " DESC")
        while(cursor?.moveToNext()!!){
            val dataText = MyDbString()
            dataText.user = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER))
            dataText.score = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_SCORE))
            dataText.date = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE))
            dataList.add(dataText)
        }
        cursor.close()
        return dataList
    }

    @SuppressLint("Range")
    fun readDbUserData() :ArrayList<String>{
        val dataList = ArrayList<String>()
        val cursor = db?.query(TABLE_NAME2, null,null,null,null,null,null)
        while(cursor?.moveToNext()!!){
            val userName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USERNAME))
            dataList.add(userName)
        }
        cursor.close()
        return dataList
    }

    fun closeDb()
    {
        myDbHelper.close();
    }
}