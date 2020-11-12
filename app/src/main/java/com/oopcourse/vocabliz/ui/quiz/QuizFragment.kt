//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.ui.quiz

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.oopcourse.vocabliz.R
import com.oopcourse.vocabliz.data.Word
import com.oopcourse.vocabliz.databinding.QuizFragmentBinding
import com.oopcourse.vocabliz.ui.wordlist.QuizWordListViewModel


class QuizFragment : Fragment() {


    private val viewModel: QuizWordListViewModel by activityViewModels()
    private val binding: QuizFragmentBinding by lazy { QuizFragmentBinding.inflate(this.layoutInflater) }

    private val submitter = object : QuizSubmitter {
        override fun submitQuiz() {
            view?.findNavController()?.navigate(R.id.action_quizFragment_to_quizResultFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.lifecycleOwner = this
        binding.quizViewModel = viewModel

        val wordIterator = viewModel.listForQuiz.value?.iterator()

        binding.userGuessInput.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.getUserGuess(binding.userGuessInput.text)
                if (viewModel.isTranslation()) acceptAnswerAnimation(wordIterator!!) else denyAnswerAnimation()
                binding.userGuessInput.text = null
                return@OnEditorActionListener true
            }
            false
        })

        binding.skipButton.setOnClickListener {
            viewModel.getNextWord(wordIterator!!, submitter)
        }


        val listObserver = Observer<List<Word>> { newWords ->
            // Update the UI
            viewModel.getNextWord(wordIterator!!, submitter)
            viewModel.resetScore()
        }
        viewModel.listForQuiz.observe(viewLifecycleOwner, listObserver)

        val wordObserver = Observer<Word> { newWords ->
            // Update the UI
            if (viewModel.currentWord.value == viewModel.listForQuiz.value?.last()) { //this is not good
                binding.skipButton.text = "Submit Quiz"
                binding.skipButton.setOnClickListener { view: View ->
                    submitter.submitQuiz()
                }
            }
        }
        viewModel.currentWord.observe(viewLifecycleOwner, wordObserver)

        return binding.root
    }


    //This methods should not be living in this file, but I didnt have enough time for research
    //and decided to implement them straight foreward over here((

    private fun acceptAnswerAnimation(wordIterator: Iterator<Word>) {
        val runnable = Runnable {
            viewModel.getNextWord(wordIterator, submitter)
            binding.quizCardBackground.setBackgroundResource(R.drawable.background_test)
        }

        showToast(this.requireView(), "RIGHT")
        viewModel.timer.value?.cancel()
        viewModel.countScore()
        binding.quizCardBackground.setBackgroundColor(Color.GREEN)
        this.view?.postDelayed(runnable, 1000)

    }

    private fun denyAnswerAnimation() {
        val denyAnswer = Runnable {
            binding.quizCardBackground.setBackgroundResource(R.drawable.background_test)
        }
        showToast(this.requireView(), "WRONG")
        binding.quizCardBackground.setBackgroundColor(Color.RED)
        this.view?.postDelayed(denyAnswer, 1000)
    }

    private fun showToast(view: View, text: String) {
        val toastDurationInMilliSeconds = 1000
        val mToastToShow = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        mToastToShow.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
        val toastCountDown: CountDownTimer
        toastCountDown = object : CountDownTimer(
            toastDurationInMilliSeconds.toLong(), 5000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                mToastToShow.show()
            }
            override fun onFinish() {
                mToastToShow.cancel()
            }
        }
        mToastToShow.show()
        toastCountDown.start()
    }


}









