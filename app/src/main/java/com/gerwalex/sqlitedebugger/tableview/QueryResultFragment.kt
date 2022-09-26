package com.gerwalex.sqlitedebugger.tableview

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
import kotlinx.coroutines.launch

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
            mAdapter.swap(viewModel.getDao().query(args.query))
        }
    }
}
