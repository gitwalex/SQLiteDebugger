package com.gerwalex.sqlitedebugger.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class Dao(private val db: SQLiteDatabase) {

    fun getTablesNames(): Cursor {
        return db.rawQuery("select * from sqlite_master " +
                "where name not in ('android_metadata', 'room_master_table' ) " +
                "and name not like 'sqlite_%' " +
                "and type in ('table', 'view')", null, null)
    }

    fun getTableInfo(table: String): Cursor {
        return db.rawQuery("pragma table_info($table)", null, null)
    }

    fun getCount(name: String): Long {
        db.rawQuery("select Count(*) from $name", null, null).use { c ->
            return if (c.moveToFirst()) c.getLong(0) else 0
        }
    }

    fun get(query: String): Cursor {
        return db.rawQuery(query, null, null)
    }

    fun query(query: String): Cursor {
        return db.rawQuery(query, null, null)
    }
}