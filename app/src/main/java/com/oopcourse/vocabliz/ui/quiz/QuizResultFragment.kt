//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.ui.quiz

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
import com.oopcourse.vocabliz.databinding.QuizzResultFragmentBinding
import com.oopcourse.vocabliz.ui.wordlist.QuizWordListViewModel




class QuizResultFragment: Fragment() {

    private val viewModel: QuizWordListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding:QuizzResultFragmentBinding = DataBindingUtil
            .inflate<QuizzResultFragmentBinding>(inflater,R
                .layout.quizz_result_fragment,container,false)
        // Inflate the layout for this fragment
        binding.lifecycleOwner = this
        binding.quizViewModel = viewModel

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            view?.findNavController()?.navigate(R.id.action_quizResultFragment_to_quizWordListActivity)
        }


        val scoreObserver = Observer<Int>{

        }
        viewModel.quizScore.observe(viewLifecycleOwner,scoreObserver)


        return binding.root
    }


}