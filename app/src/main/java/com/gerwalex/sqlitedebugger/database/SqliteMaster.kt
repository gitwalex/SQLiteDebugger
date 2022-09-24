package com.gerwalex.sqlitedebugger.database

import android.database.Cursor
import com.gerwalex.database.ObservableTableRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class SqliteMaster(val cursor: Cursor) : ObservableTableRow(cursor) {

    val name: String
    val type: String
    val sql: String
    val tableInfo = ArrayList<TableInfo>()
    var count: Long = 0
    suspend fun fillTableInfo(dao: Dao) {
        withContext(Dispatchers.IO) {
            count = dao.getCount(name)
            dao.getTableInfo(name).use { c ->
                if (c.moveToFirst()) {
                    do {
                        tableInfo.add(TableInfo(c))
                    } while (c.moveToNext())
                }
            }
        }
    }

    init {
        name = getAsString("tbl_name")!!
        type = getAsString("type")!!
        sql = getAsString("sql")!!
    }
}