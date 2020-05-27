package com.lomza.moviesroom.movie

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lomza.moviesroom.R
import com.lomza.moviesroom.db.Director
import com.lomza.moviesroom.db.Movie
import com.lomza.moviesroom.db.MoviesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author Antonina
 */
class MovieSaveDialogFragment : DialogFragment() {

    private var movieTitleExtra: String? = null
    private var movieDirectorFullNameExtra: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieTitleExtra = arguments!!.getString(EXTRA_MOVIE_TITLE)
        movieDirectorFullNameExtra = arguments!!.getString(EXTRA_MOVIE_DIRECTOR_FULL_NAME)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_movie, null)
        val movieEditText = view.findViewById<EditText>(R.id.etMovieTitle)
        val movieDirectorEditText = view.findViewById<EditText>(R.id.etMovieDirectorFullName)
        if (movieTitleExtra != null) {
            movieEditText.setText(movieTitleExtra)
            movieEditText.setSelection(movieTitleExtra!!.length)
        }
        if (movieDirectorFullNameExtra != null) {
            movieDirectorEditText.setText(movieDirectorFullNameExtra)
            movieDirectorEditText.setSelection(movieDirectorFullNameExtra!!.length)
        }
        alertDialogBuilder.setView(view)
            .setTitle(getString(R.string.dialog_movie_title))
            .setPositiveButton(R.string.save) { _, _ ->
                GlobalScope.launch(Dispatchers.IO) { saveMovie(movieEditText.text.toString(), movieDirectorEditText.text.toString()) }
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }

        return alertDialogBuilder.create()
    }

    private suspend fun saveMovie(movieTitle: String, movieDirectorFullName: String) {
        if (TextUtils.isEmpty(movieTitle) || TextUtils.isEmpty(movieDirectorFullName)) {
            return
        }
        val directorDao = MoviesDatabase.getDatabase(requireContext()).directorDao()
        val movieDao = MoviesDatabase.getDatabase(requireContext()).movieDao()
        var directorId: Long = -1L
        if (movieDirectorFullNameExtra != null) {
            // clicked on item row -> update
            val directorToUpdate = directorDao.findDirectorByName(movieDirectorFullNameExtra)
            if (directorToUpdate != null) {
                directorId = directorToUpdate.id
                if (directorToUpdate.fullName != movieDirectorFullName) {
                    directorToUpdate.fullName = movieDirectorFullName
                    directorDao.update(directorToUpdate)
                }
            }
        } else {
            // we need director id for movie object; in case director is already in DB,
            // insert() would return -1, so we manually check if it exists and get
            // the id of already saved director
            val newDirector = directorDao.findDirectorByName(movieDirectorFullName)
            directorId = newDirector?.id ?: directorDao.insert(Director(fullName = movieDirectorFullName))
        }

        if (movieTitleExtra != null) {
            // clicked on item row -> update
            val movieToUpdate = movieDao.findMovieByTitle(movieTitleExtra)
            if (movieToUpdate != null) {
                if (movieToUpdate.title != movieTitle) {
                    movieToUpdate.title = movieTitle
                    if (directorId != -1L) {
                        movieToUpdate.directorId = directorId
                    }
                    movieDao.update(movieToUpdate)
                }
            }
        } else {
            // we can have many movies with same title but different director
            movieDao.insert(Movie(title = movieTitle, directorId = directorId))
        }
    }

    companion object {
        private const val EXTRA_MOVIE_TITLE = "movie_title"
        private const val EXTRA_MOVIE_DIRECTOR_FULL_NAME = "movie_director_full_name"
        const val TAG_DIALOG_MOVIE_SAVE = "dialog_movie_save"

        fun newInstance(movieTitle: String?, movieDirectorFullName: String?): MovieSaveDialogFragment {
            val fragment = MovieSaveDialogFragment()
            val args = Bundle().apply {
                putString(EXTRA_MOVIE_TITLE, movieTitle)
                putString(EXTRA_MOVIE_DIRECTOR_FULL_NAME, movieDirectorFullName)
            }
            fragment.arguments = args
            return fragment
        }
    }
}