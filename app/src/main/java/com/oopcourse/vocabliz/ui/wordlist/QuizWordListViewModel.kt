//Georg Vassilev
//1807282

package com.oopcourse.vocabliz.ui.wordlist


import android.os.CountDownTimer
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oopcourse.vocabliz.data.Word
import com.oopcourse.vocabliz.network.WordsApi
import com.oopcourse.vocabliz.ui.quiz.QuizSubmitter

import kotlinx.coroutines.launch


class QuizWordListViewModel() : ViewModel() {

    private var _listForQuiz = MutableLiveData<List<Word>>()
    val listForQuiz: LiveData<List<Word>>
        get() = _listForQuiz

    private var _timer = MutableLiveData<CountDownTimer>()
    val timer: LiveData<CountDownTimer>
        get() = _timer
    private var _time = MutableLiveData<Long>()
    val time: LiveData<Long>
        get() = _time

    private var _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = _currentWord

    private var _currentWordIndex = MutableLiveData<Int>()
    val currentWordIndex: LiveData<Int>
        get() = _currentWordIndex

    private var _userGuess = MutableLiveData<String>()
    private val userGuess: LiveData<String>
        get() = _userGuess

    private var _languagesAvailable = MutableLiveData<List<String>>()
    val languagesAvailable: LiveData<List<String>>
        get() = _languagesAvailable
    private var _chosenLanguage = MutableLiveData<String>()
    val chosenLanguage: LiveData<String>
        get() = _chosenLanguage

    private var _quizScore = MutableLiveData<Int>()
    val quizScore: LiveData<Int>
        get() = _quizScore

    init {
        getWordsFromNet()
        _chosenLanguage.value = "english"
    }


    fun chooseLanguage(lang: String) {
        _chosenLanguage.value = lang
    }

    fun getWordsFromNet() {
        viewModelScope.launch {
            try {
                val listResult = WordsApi.retrofitService.getProperties()
                if (listResult.size > 0) {
                    _listForQuiz.value = listResult.shuffled().take(10)
                    _languagesAvailable.value = listResult.first().translations.map { it.lang }

                }
            } catch (e: Exception) {
                // TODO: 02.10.2020

            }
        }
    }


    fun getNextWord(wordIterator: Iterator<Word>, quizSubmitter: QuizSubmitter) {

        if (wordIterator.hasNext()) {
            _currentWord.value = wordIterator.next()
            _timer.value?.cancel()
            getNewTimer(wordIterator, quizSubmitter)
            getWordIndex()
        } else {
            quizSubmitter.submitQuiz()
        }
    }

    fun countScore() {
        if (isTranslation()) {
            if (time.value!! >= 10) {
                _quizScore.value = _quizScore.value?.plus(10)
            } else {
                _quizScore.value = _quizScore.value?.plus(time.value!!.toInt())
            }
        }
    }

    fun resetScore() {
        _quizScore.value = 0
    }

    private fun getWordIndex() {
        _currentWordIndex.value = listForQuiz.value?.indexOf(currentWord.value)
    }

    fun getUserGuess(input: Editable) {
        _userGuess.value = input.toString()
    }

    fun isTranslation(): Boolean {
        return userGuess.value == currentWord.value?.translations?.first { it.lang == chosenLanguage.value }?.text
    }

    private fun getNewTimer(wordIterator: Iterator<Word>, quizSubmitter: QuizSubmitter) {

        _timer.value = object : CountDownTimer(16000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _time.value = millisUntilFinished / 1000

            }

            override fun onFinish() {
                getNextWord(wordIterator, quizSubmitter)
                cancel()
            }
        }.start()

    }


}
