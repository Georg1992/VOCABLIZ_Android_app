//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oopcourse.vocabliz.R



class PopUpMenuAdapter(private val list: List<String>) : RecyclerView.Adapter<PopUpMenuAdapter.PopUpViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopUpViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.pop_menu_item_view, parent, false) as TextView
        return PopUpViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopUpViewHolder, position: Int) {
        val lang:String = list[position]
        holder.textView.text = lang
    }

    override fun getItemCount(): Int = list.size


    inner class PopUpViewHolder(val textView: TextView):RecyclerView.ViewHolder(textView) {
        init {
            textView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }
    }

}




