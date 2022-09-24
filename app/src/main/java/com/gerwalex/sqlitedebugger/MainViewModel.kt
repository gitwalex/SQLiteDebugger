package com.gerwalex.sqlitedebugger

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gerwalex.sqlitedebugger.database.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.FileOutputStream

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var dbname: String? = null
    private lateinit var helper: DBOpenHelper
    private var job: Job? = null
    val context = getApplication<Application>()

    @Throws(FileNotFoundException::class)
    fun copyDatabase(uri: Uri) {
        dbname?.let {
            val input = context.contentResolver.openInputStream(uri)// throws FileNotFoundException
            val outFile = context.getDatabasePath(dbname)
            job = viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    input?.use {
                        val out = FileOutputStream(outFile)
                        Log.d("gerwalex", "out:$out")
                        it.copyTo(out, 8096)
                    }
                }
            }
        } ?: throw java.lang.IllegalStateException("DBName not set")
    }

    @Throws(IllegalStateException::class)
    suspend fun getDao(): Dao {
        dbname?.let {
            job?.join()
            helper = DBOpenHelper(context, it)
            return helper.dao
        } ?: throw java.lang.IllegalStateException("DBName not set")
    }

    inner class DBOpenHelper(context: Context, name: String) : SQLiteOpenHelper(context, name, null, 1) {

        val dao = Dao(writableDatabase)
        override fun onCreate(db: SQLiteDatabase?) {}
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
    }
}
