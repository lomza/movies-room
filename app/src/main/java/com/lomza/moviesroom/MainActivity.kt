package com.lomza.moviesroom

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lomza.moviesroom.db.MoviesDatabase
import com.lomza.moviesroom.db.rePopulateDb
import com.lomza.moviesroom.director.DirectorSaveDialogFragment
import com.lomza.moviesroom.director.DirectorsListFragment
import com.lomza.moviesroom.movie.MovieSaveDialogFragment
import com.lomza.moviesroom.movie.MoviesListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var shownFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar(getString(R.string.app_name))
        initView()
        if (savedInstanceState == null) {
            showFragment(MoviesListFragment.newInstance())
        }
    }

    private fun initToolbar(@NonNull title: String?) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    private fun initView() {
        navigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_movies -> {
                        MOVIES_SHOWN = true
                        showFragment(MoviesListFragment.newInstance())
                        return true
                    }
                    R.id.navigation_directors -> {
                        MOVIES_SHOWN = false
                        showFragment(DirectorsListFragment.newInstance())
                        return true
                    }
                }
                return false
            }
        })
        fab.setOnClickListener { showSaveDialog() }
    }

    private fun showFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentHolder, fragment)
        fragmentTransaction.commitNow()
        shownFragment = fragment
    }

    private fun showSaveDialog() {
        val dialogFragment: DialogFragment
        val tag: String
        if (MOVIES_SHOWN) {
            dialogFragment = MovieSaveDialogFragment.newInstance(null, null)
            tag = MovieSaveDialogFragment.TAG_DIALOG_MOVIE_SAVE
        } else {
            dialogFragment = DirectorSaveDialogFragment.newInstance(null)
            tag = DirectorSaveDialogFragment.TAG_DIALOG_DIRECTOR_SAVE
        }
        dialogFragment.show(supportFragmentManager, tag)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_list_data -> {
                deleteCurrentListData()
                true
            }
            R.id.action_re_create_database -> {
                reCreateDatabase()
                true
            }
            R.id.action_export_to_csv_file -> {
                exportDatabaseToCSVFile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteCurrentListData() {
        if (MOVIES_SHOWN) {
            (shownFragment as MoviesListFragment).removeData()
        } else {
            (shownFragment as DirectorsListFragment).removeData()
        }
    }

    private fun reCreateDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            rePopulateDb(MoviesDatabase.getDatabase(this@MainActivity))
        }
    }
    private fun getCSVFileName() : String =
        if (MOVIES_SHOWN) "MoviesRoomExample.csv" else "DirectorsRoomExample.csv"

    private fun exportDatabaseToCSVFile() {
        val csvFile = generateFile(this, getCSVFileName())
        if (csvFile != null) {
            if (MOVIES_SHOWN) {
                (shownFragment as MoviesListFragment).exportMoviesWithDirectorsToCSVFile(csvFile)
            } else {
                (shownFragment as DirectorsListFragment).exportDirectorsToCSVFile(csvFile)
            }

            Toast.makeText(this, getString(R.string.csv_file_generated_text), Toast.LENGTH_LONG).show()
            val intent = goToFileIntent(this, csvFile)
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.csv_file_not_generated_text), Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private var MOVIES_SHOWN = true
    }
}