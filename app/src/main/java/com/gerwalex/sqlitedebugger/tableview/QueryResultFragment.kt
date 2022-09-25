package com.gerwalex.sqlitedebugger.tableview

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.gerwalex.sqlitedebugger.MainViewModel
import com.gerwalex.sqlitedebugger.databinding.QueryresultFragmentBinding
import com.gerwalex.sqlitedebugger.tableview.model.Cell
import com.gerwalex.sqlitedebugger.tableview.model.ColumnHeader
import com.gerwalex.sqlitedebugger.tableview.model.RowHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class QueryResultFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    private val args: QueryResultFragmentArgs by navArgs()
    private lateinit var binding: QueryresultFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("gerwalex", "query: ${args.query}")
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = QueryresultFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = TableViewAdapter()
        binding.tableview.setAdapter(mAdapter)
        viewLifecycleOwner.lifecycleScope.launch {
            val colList: MutableList<ColumnHeader> = ArrayList()
            val rowList: MutableList<RowHeader> = ArrayList()
            val cellLists: MutableList<List<Cell>> = ArrayList()
            withContext(Dispatchers.IO) {
                viewModel.getDao().query(args.query).use { c ->
                    for (index in 0 until c.columnCount) {
                        colList.add(ColumnHeader(index.toString(), c.getColumnName(index)))
                    }
                    if (c.moveToFirst()) {
                        do {
                            rowList.add(RowHeader(c.getString(0), c.getString(0)))
                            val cellList: MutableList<Cell> = ArrayList()
                            for (index in 0 until c.columnCount) {
                                cellList.add(Cell(index.toString(), c.getString(index)))
                            }
                            cellLists.add(cellList)
                        } while (c.moveToNext())
                    }
                }
            }
            mAdapter.setAllItems(colList, rowList, cellLists)
        }
    }
}
