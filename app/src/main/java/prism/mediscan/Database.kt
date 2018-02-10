package prism.mediscan

/**
 * Created by rapha on 10/02/2018.
 */

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import prism.mediscan.model.Scan


class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w(TAG, "Updating table from " + oldVersion + " to " + newVersion)
        try {
            // nothing to do
            db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY;")
            onCreate(db)
        } catch (exception: Exception) {
            Log.e(TAG, "Exception running upgrade script:", exception)
        }
    }

    fun storeScan(scan: Scan): Scan? {
        var result: Scan? = null
        val values = ContentValues()
        values.put(COLUMN_CIP, scan.cip)
        values.put(COLUMN_TIMESTAMP, scan.timestamp)
        val inserId = writableDatabase.insert(TABLE_HISTORY, null, values)
        val cursor = readableDatabase.query(TABLE_HISTORY, arrayOf(COLUMN_CIP, COLUMN_TIMESTAMP), COLUMN_ID + " = ?", arrayOf(inserId.toString()), null, null, null)
        if (cursor.moveToFirst()) {
            result = cursorToScan(cursor)
        }
        cursor.close()
        return result
    }

    fun cursorToScan(cursor: Cursor): Scan {
        Log.d("Database", cursor.getString(0))
        val scan = Scan(cursor.getString(cursor.getColumnIndex(COLUMN_CIP)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)).toLong(10))
        return scan
    }

    fun getAllScans(): List<Scan> {
        val cursor = readableDatabase.query(TABLE_HISTORY, arrayOf(COLUMN_CIP, COLUMN_TIMESTAMP), null, null, null, null, null)
        val list = ArrayList<Scan>(cursor.count)
        while (cursor.moveToNext()) {
            list.add(cursorToScan(cursor))
        }
        cursor.close()
        return list
    }

    companion object {
        val TAG = Database::class.java.name

        val TABLE_HISTORY = "comments"
        val COLUMN_ID = "id"
        val COLUMN_CIP = "comment"
        val COLUMN_TIMESTAMP = "timestamp"

        private val DATABASE_NAME = "mediscan.db"
        private val DATABASE_VERSION = 2

        // Database creation sql statement
        private val DATABASE_CREATE = ("create table "
                + TABLE_HISTORY + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_CIP
                + " text not null, " + COLUMN_TIMESTAMP
                + " integer not null);")
    }

}
