package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    val questionList = listOf(
        Question(
            R.string.question_1,
            answerList = listOf(
                Answer(R.string.answer_1_0, isCorrect = true),
                Answer(R.string.answer_1_1, isCorrect = false),
                Answer(R.string.answer_1_2, isCorrect = false),
                Answer(R.string.answer_1_3, isCorrect = false)
            )
        ),
        Question(
            R.string.question_2,
            answerList = listOf(
                Answer(R.string.answer_2_0, isCorrect = false),
                Answer(R.string.answer_2_1, isCorrect = true),
                Answer(R.string.answer_2_2, isCorrect = false),
                Answer(R.string.answer_2_3, isCorrect = false)
            )
        ),
        Question(
            questionResId = R.string.question_3,
            answerList = listOf(
                Answer(R.string.answer_3_0, isCorrect = false),
                Answer(R.string.answer_3_1, isCorrect = false),
                Answer(R.string.answer_3_2, isCorrect = true),
                Answer(R.string.answer_3_3, isCorrect = false)
            )
        ),
        Question(
            questionResId = R.string.question_4,
            answerList = listOf(
                Answer(R.string.answer_4_0, isCorrect = false),
                Answer(R.string.answer_4_1, isCorrect = false),
                Answer(R.string.answer_4_2, isCorrect = false),
                Answer(R.string.answer_4_3, isCorrect = true)
            )
        )
    )

    var currentQuestionIndex = 0

    val currentQuestion: Question
        get() = questionList[currentQuestionIndex]

    fun moveToNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size
    }


    fun resetAllQuestions() {
        currentQuestionIndex = 0
        questionList.forEach { question ->
            question.answerList.forEach { answer ->
                answer.isEnabled = true
                answer.isSelected = false
            }
        }
    }

    val isLastQuestion: Boolean
        get() = currentQuestionIndex == questionList.size - 1
}
