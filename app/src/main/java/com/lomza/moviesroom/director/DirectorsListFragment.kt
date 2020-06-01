package com.lomza.moviesroom.director

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.lomza.moviesroom.R
import com.lomza.moviesroom.db.Director
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author Antonina
 */
class DirectorsListFragment : Fragment() {

    private lateinit var directorsListAdapter: DirectorsListAdapter
    private lateinit var directorsViewModel: DirectorsViewModel
    private lateinit var directorsList: List<Director>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_directors, container, false)
        initView(view)

        return view
    }

    private fun initData() {
        directorsViewModel = ViewModelProvider(this).get(DirectorsViewModel::class.java)
        directorsViewModel.directorList.observe(this,
            Observer { directors: List<Director> ->
                directorsList = directors
                directorsListAdapter.setDirectorList(directors)
            }
        )
    }

    private fun initView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_directors)
        directorsListAdapter = DirectorsListAdapter(requireContext())
        recyclerView.adapter = directorsListAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun removeData() {
        GlobalScope.launch(Dispatchers.IO) { directorsViewModel.deleteAll() }
    }

    fun exportDirectorsToCSVFile(csvFile: File) {
        csvWriter().open(csvFile, append = false) {
            // Header
            writeRow(listOf("[id]", "[${Director.TABLE_NAME}]"))
            directorsList.forEachIndexed { index, director ->
                writeRow(listOf(index, director.fullName))
            }
        }
    }

    companion object {
        fun newInstance(): DirectorsListFragment = DirectorsListFragment()
    }
}