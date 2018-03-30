package com.lomza.moviesroom.movie;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lomza.moviesroom.R;
import com.lomza.moviesroom.db.Director;
import com.lomza.moviesroom.db.Movie;
import com.lomza.moviesroom.db.MoviesDatabase;

import java.util.List;

import static com.lomza.moviesroom.movie.MovieSaveDialogFragment.TAG_DIALOG_MOVIE_SAVE;

/**
 * @author Antonina
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Movie> movieList;
    private Context context;

    public MoviesListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = layoutInflater.inflate(R.layout.item_list_movie, parent, false);
        return new MoviesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        if (movieList == null) {
            return;
        }

        final Movie movie = movieList.get(position);
        if (movie != null) {
            holder.titleText.setText(movie.title);

            final Director director = MoviesDatabase.getDatabase(context).directorDao().findDirectorById(movie.directorId);
            final String directorFullName;
            if (director != null) {
                holder.directorText.setText(director.fullName);
                directorFullName = director.fullName;
            } else {
                directorFullName = "";
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialogFragment = MovieSaveDialogFragment.newInstance(movie.title, directorFullName);
                    dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), TAG_DIALOG_MOVIE_SAVE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        } else {
            return movieList.size();
        }
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView directorText;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.tvMovieTitle);
            directorText = itemView.findViewById(R.id.tvMovieDirectorFullName);
        }
    }
}
