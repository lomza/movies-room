package com.lomza.moviesroom.movie;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lomza.moviesroom.R;
import com.lomza.moviesroom.db.Director;
import com.lomza.moviesroom.db.DirectorDao;
import com.lomza.moviesroom.db.Movie;
import com.lomza.moviesroom.db.MovieDao;
import com.lomza.moviesroom.db.MoviesDatabase;

/**
 * @author Antonina
 */

public class MovieSaveDialogFragment extends DialogFragment {
    private Context context;
    private String movieTitleExtra;
    private String movieDirectorFullNameExtra;

    private static final String EXTRA_MOVIE_TITLE = "movie_title";
    private static final String EXTRA_MOVIE_DIRECTOR_FULL_NAME = "movie_director_full_name";
    public static final String TAG_DIALOG_MOVIE_SAVE = "dialog_movie_save";

    public static MovieSaveDialogFragment newInstance(final String movieTitle, final String movieDirectorFullName) {
        MovieSaveDialogFragment fragment = new MovieSaveDialogFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_MOVIE_TITLE, movieTitle);
        args.putString(EXTRA_MOVIE_DIRECTOR_FULL_NAME, movieDirectorFullName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        movieTitleExtra = args.getString(EXTRA_MOVIE_TITLE);
        movieDirectorFullNameExtra = args.getString(EXTRA_MOVIE_DIRECTOR_FULL_NAME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_movie, null);
        final EditText movieEditText = view.findViewById(R.id.etMovieTitle);
        final EditText movieDirectorEditText = view.findViewById(R.id.etMovieDirectorFullName);
        if (movieTitleExtra != null) {
            movieEditText.setText(movieTitleExtra);
            movieEditText.setSelection(movieTitleExtra.length());
        }
        if (movieDirectorFullNameExtra != null) {
            movieDirectorEditText.setText(movieDirectorFullNameExtra);
            movieDirectorEditText.setSelection(movieDirectorFullNameExtra.length());
        }

        alertDialogBuilder.setView(view)
                .setTitle(getString(R.string.dialog_movie_title))
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveMovie(movieEditText.getText().toString(), movieDirectorEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return alertDialogBuilder.create();
    }

    private void saveMovie(String movieTitle, String movieDirectorFullName) {
        if (TextUtils.isEmpty(movieTitle) || TextUtils.isEmpty(movieDirectorFullName)) {
            return;
        }

        DirectorDao directorDao = MoviesDatabase.getDatabase(context).directorDao();
        MovieDao movieDao = MoviesDatabase.getDatabase(context).movieDao();

        int directorId = -1;
        if (movieDirectorFullNameExtra != null) {
            // clicked on item row -> update
            Director directorToUpdate = directorDao.findDirectorByName(movieDirectorFullNameExtra);
            if (directorToUpdate != null) {
                directorId = directorToUpdate.id;

                if (!directorToUpdate.fullName.equals(movieDirectorFullName)) {
                    directorToUpdate.fullName = movieDirectorFullName;
                    directorDao.update(directorToUpdate);
                }
            }
        } else {
            // we need director id for movie object; in case director is already in DB,
            // insert() would return -1, so we manually check if it exists and get
            // the id of already saved director
            Director newDirector = directorDao.findDirectorByName(movieDirectorFullName);
            if (newDirector == null) {
                directorId = (int) directorDao.insert(new Director(movieDirectorFullName));
            } else {
                directorId = newDirector.id;
            }
        }

        if (movieTitleExtra != null) {
            // clicked on item row -> update
            Movie movieToUpdate = movieDao.findMovieByTitle(movieTitleExtra);
            if (movieToUpdate != null) {
                if (!movieToUpdate.title.equals(movieTitle)) {
                    movieToUpdate.title = movieTitle;
                    if (directorId != -1) {
                        movieToUpdate.directorId = directorId;
                    }
                    movieDao.update(movieToUpdate);
                }
            }
        } else {
            // we can have many movies with same title but different director
            Movie newMovie = movieDao.findMovieByTitle(movieTitle);
            if (newMovie == null) {
                movieDao.insert(new Movie(movieTitle, directorId));
            } else {
                if (newMovie.directorId != directorId) {
                    newMovie.directorId = directorId;
                    movieDao.update(newMovie);
                }
            }
        }
    }
}
