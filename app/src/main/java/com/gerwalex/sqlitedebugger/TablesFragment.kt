package com.gerwalex.sqlitedebugger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.gerwalex.recyclerviewadapters.ItemListAdapter
import com.gerwalex.recyclerviewadapters.ViewHolder
import com.gerwalex.sqlitedebugger.database.SqliteMaster
import com.gerwalex.sqlitedebugger.databinding.TablesFragmentBinding
import com.gerwalex.sqlitedebugger.databinding.TablesItemBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TablesFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: TablesFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TablesFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            val mAdapter = Adapter()
            this.adapter = mAdapter
            viewLifecycleOwner.lifecycleScope.launch {
                val list = ArrayList<SqliteMaster>()
                withContext(Dispatchers.IO) {
                    try {
                        with(viewModel.getDao()) {
                            getTablesNames().use { c ->
                                if (c.moveToFirst()) {
                                    do {
                                        val item = SqliteMaster(c)
                                        list.add(item)
                                        item.fillTableInfo(this)
                                    } while (c.moveToNext())
                                }
                            }
                        }
                    } catch (e: java.lang.IllegalStateException) {
                        Snackbar.make(binding.root, R.string.noDatabase, Snackbar.LENGTH_LONG).show()
                    }
                }
                mAdapter.addAll(list)
            }
        }
    }

    private inner class Adapter : ItemListAdapter<SqliteMaster>() {

        override fun getItemViewType(position: Int): Int {
            return R.layout.tables_item
        }

        override fun onBindViewHolder(holder: ViewHolder, item: SqliteMaster, position: Int) {
            with(holder.binding as TablesItemBinding) {
                this.item = item
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, itemType: Int): ViewHolder {
            val holder = super.onCreateViewHolder(viewGroup, itemType)
            holder.itemView.setOnClickListener {
                val position = getPosition(it)
                val item = getItemAt(position)
                val action = TablesFragmentDirections.actionTablesFragmentToQueryResultFragment(
                    "select * from ${item.name}")
                viewGroup.findNavController().navigate(action)
            }
            return holder
        }
    }
}