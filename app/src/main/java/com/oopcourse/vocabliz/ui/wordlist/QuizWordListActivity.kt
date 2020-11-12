//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.ui.wordlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.oopcourse.vocabliz.R
import com.oopcourse.vocabliz.data.Word
import com.oopcourse.vocabliz.databinding.QuizWordListActivityBinding


class QuizWordListActivity : Fragment(),LanguageGetter {

    private val viewModel: QuizWordListViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            view?.findNavController()?.navigate(R.id.action_quizWordListActivity_to_titleFragment2)
        }



        val binding = DataBindingUtil
            .inflate<QuizWordListActivityBinding>(inflater,R.layout
                .quiz_word_list_activity,container,false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var langGetter = object : LanguageGetter
        {override fun getLanguage(): String? = viewModel.chosenLanguage.value}
        val adapter = WordListAdapter(langGetter)
        binding.wordList.adapter = adapter


        binding.shuffleButton.setOnClickListener { viewModel.getWordsFromNet() }
        binding.playQuiz.setOnClickListener {view:View ->
            view.findNavController().navigate(R.id.action_quizWordListActivity_to_quizFragment)
        }

        val listObserver = Observer<List<Word>> { newWords ->
            // Update the UI
            adapter.submitList(viewModel.listForQuiz.value)
        }
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.listForQuiz.observe(viewLifecycleOwner, listObserver)

        return binding.root

    }









}


