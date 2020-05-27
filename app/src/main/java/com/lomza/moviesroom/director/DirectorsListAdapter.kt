package com.lomza.moviesroom.director

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.lomza.moviesroom.R
import com.lomza.moviesroom.db.Director
import com.lomza.moviesroom.director.DirectorsListAdapter.DirectorsViewHolder

/**
 * @author Antonina
 */
class DirectorsListAdapter(val context: Context) : RecyclerView.Adapter<DirectorsViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var directorList: List<Director>? = null

    fun setDirectorList(directorList: List<Director>) {
        this.directorList = directorList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectorsViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_list_director, parent, false)
        return DirectorsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DirectorsViewHolder, position: Int) {
        directorList?.let {
            val director = it[position]
            holder.directorText.text = director.fullName
            holder.itemView.setOnClickListener {
                val dialogFragment: DialogFragment = DirectorSaveDialogFragment.newInstance(director.fullName)
                dialogFragment.show(
                    (context as AppCompatActivity).supportFragmentManager,
                    DirectorSaveDialogFragment.TAG_DIALOG_DIRECTOR_SAVE
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return if (directorList == null) {
            0
        } else {
            directorList!!.size
        }
    }

    class DirectorsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val directorText: TextView = itemView.findViewById(R.id.tvDirector)
    }
}