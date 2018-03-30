package com.lomza.moviesroom.director;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lomza.moviesroom.R;
import com.lomza.moviesroom.db.Director;

import java.util.List;

/**
 * @author Antonina
 */

public class DirectorsListFragment extends Fragment {
    private DirectorsListAdapter directorsListAdapter;
    private DirectorsViewModel directorsViewModel;
    private Context context;

    public static DirectorsListFragment newInstance() {
        return new DirectorsListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        directorsListAdapter = new DirectorsListAdapter(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directors, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_directors);
        recyclerView.setAdapter(directorsListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    private void initData() {
        directorsViewModel = ViewModelProviders.of(this).get(DirectorsViewModel.class);
        directorsViewModel.getDirectorList().observe(this, new Observer<List<Director>>() {
            @Override
            public void onChanged(@Nullable List<Director> directors) {
                directorsListAdapter.setDirectorList(directors);
            }
        });
    }

    public void removeData() {
        if (directorsViewModel != null) {
            directorsViewModel.deleteAll();
        }
    }
}
