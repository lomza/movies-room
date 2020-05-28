package com.lomza.moviesroom.movie

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
import com.lomza.moviesroom.R
import com.lomza.moviesroom.db.Movie
import com.lomza.moviesroom.db.MoviesDatabase
import kotlinx.coroutines.*

/**
 * @author Antonina
 */
class MoviesListFragment : Fragment() {

    private lateinit var moviesListAdapter: MoviesListAdapter
    private lateinit var moviesViewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movies, container, false)
        initView(view)

        return view
    }

    private fun initData() {
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        moviesViewModel.moviesList.observe(this,
            Observer { movies: List<Movie> ->
                moviesListAdapter.setMovieList(movies)
            }
        )

        moviesViewModel.directorsList.observe(this,
            Observer { _ ->
                // we need to refresh the movies list in case when director's name changed
                moviesViewModel.moviesList.value?.let {
                    moviesListAdapter.setMovieList(it)
                }
            }
        )
    }

    private suspend fun getDirectorFullName(movie: Movie): String? {
        return MoviesDatabase.getDatabase(requireContext()).directorDao().findDirectorById(movie.directorId)?.fullName
    }

    private fun initView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_movies)
        moviesListAdapter = MoviesListAdapter(this)
        recyclerView.adapter = moviesListAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    fun removeData() {
        GlobalScope.launch(Dispatchers.IO) { moviesViewModel.deleteAll() }
    }

    companion object {
        fun newInstance(): MoviesListFragment = MoviesListFragment()
    }
}