package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel() {
    var isReset = false
    var score: Int = -1
    var isResetButtonEnabled = true

    fun resetQuiz() {
        isReset = true
        isResetButtonEnabled = false
    }

    fun updateScore(newScore: Int) {
        score = newScore
        isReset = false
        isResetButtonEnabled = true
    }
}