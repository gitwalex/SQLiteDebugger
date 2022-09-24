package com.gerwalex.sqlitedebugger.database

import android.database.Cursor
import com.gerwalex.database.ObservableTableRow

data class TableInfo(val cursor: Cursor) : ObservableTableRow(cursor) {

    val cid: Long
    val name: String
    val type: String
    val notNull: Boolean
    val defValue: String?
    val primaryKey: Boolean

    init {
        cid = getAsLong("cid")
        name = getAsString("name")!!
        type = getAsString("type")!!
        notNull = getAsBoolean("notNull")
        defValue = getAsString("defValue")
        primaryKey = getAsBoolean("primaryKey")
    }
}