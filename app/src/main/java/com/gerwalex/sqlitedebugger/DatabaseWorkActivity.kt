package com.gerwalex.sqlitedebugger

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.gerwalex.sqlitedebugger.databinding.DatabaseworkActivityBinding
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException

class DatabaseWorkActivity : AppCompatActivity() {

    private lateinit var dbname: String
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: DatabaseworkActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Zuerst Datanbankname ermitteln und ggfs. Uri kopieren
        savedInstanceState?.let {
            viewModel.dbname = it.getString("DBNAME")
        } ?: run {
            intent.extras?.getString("DBNAME")?.let {
                viewModel.dbname = it
            } ?: intent.extras?.getString("URI")?.let { uriString ->
                val uri = Uri.parse(uriString)
                uri.lastPathSegment?.let { dbname ->
                    viewModel.dbname = dbname
                    try {
                        viewModel.copyDatabase(uri)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        //... danach erst View festlegen, sonst ist der DBHelper nicht initialisiert
        binding = DatabaseworkActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        if (viewModel.dbname == null) {
            Snackbar.make(binding.root, R.string.noDatabase, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("DBNAME", viewModel.dbname)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}