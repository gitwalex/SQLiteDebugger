package com.gerwalex.sqlitedebugger

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.gerwalex.recyclerviewadapters.CursorAdapter
import com.gerwalex.recyclerviewadapters.ViewHolder
import com.gerwalex.sqlitedebugger.databinding.QueryresultFragmentBinding
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
        val mAdapter = QueryAdapter(LayoutInflater.from(view.context))
        binding.recyclerView.adapter = mAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                mAdapter.swap(viewModel.getDao().get(args.query))
            }
        }
    }

    inner class QueryAdapter(private val inflater: LayoutInflater) : CursorAdapter() {

        override fun getItemViewType(position: Int): Int {
            return R.layout.queryresult_items
        }

        override fun onBindViewHolder(holder: ViewHolder, mCursor: Cursor, position: Int) {
            val tvs = holder.itemView.tag as List<*>
            for ((index, value) in tvs.withIndex()) {
                (value as TextView).text = mCursor.getString(index)
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, itemType: Int): ViewHolder {
            val holder = super.onCreateViewHolder(viewGroup, itemType)

            with(holder.itemView as ViewGroup) {
                val tvs = ArrayList<TextView>()
                cursor?.let {
                    for (index in 0..it.columnCount) {
                        val tv = inflater.inflate(R.layout.resultset_textview, holder.binding.ll, true)
                        tvs.add(tv as TextView)
                    }
                }
                holder.itemView.tag = tvs
            }
            return holder
        }
    }
}