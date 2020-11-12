//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.ui.wordlist


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oopcourse.vocabliz.R
import com.oopcourse.vocabliz.data.Word



class WordListAdapter(private val langGetter:LanguageGetter) : ListAdapter<Word, TextItemViewHolder>(WordDiffCallback()) {



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.textView.text = item.text + " - " + item.translations.first { it.lang == langGetter.getLanguage() }.text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
}

class WordDiffCallback : DiffUtil.ItemCallback<Word>() {
    override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem.text == newItem.text
    }

    override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem == newItem
    }
}

class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

