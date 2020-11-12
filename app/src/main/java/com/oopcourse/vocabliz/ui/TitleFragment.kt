//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.oopcourse.vocabliz.R
import com.oopcourse.vocabliz.databinding.TitleFragmentBinding
import com.oopcourse.vocabliz.ui.wordlist.QuizWordListViewModel


class TitleFragment : Fragment() {

    private val viewModel: QuizWordListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getWordsFromNet()

        val binding = DataBindingUtil.inflate<TitleFragmentBinding>(
            inflater,
            R.layout.title_fragment, container, false
        )

        val mRecyclerView = binding.popUpMenuView
        val mLayoutManager = LinearLayoutManager(activity)

        binding.learnWordsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_titleFragment_to_quizWordListActivity)
        }

        binding.selectLangButton.setOnClickListener { lang ->
            mRecyclerView.setHasFixedSize(true)
            mRecyclerView.layoutManager = mLayoutManager
            mRecyclerView.itemAnimator = DefaultItemAnimator()
            mRecyclerView.apply {
                adapter = PopUpMenuAdapter(viewModel.languagesAvailable.value!!)
                (adapter as PopUpMenuAdapter).onItemClick = {
                    viewModel.chooseLanguage(it)
                    mRecyclerView.isVisible = false
                }
            }
            mRecyclerView.isVisible = true
        }

        return binding.root
    }
}


