package edu.vt.cs5254.multiquiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.vt.cs5254.multiquiz.databinding.ActivityMainBinding

private const val EXTRA_RESET = "edu.vt.cs5254.multiquiz.reset"

class MainActivity : AppCompatActivity() {

    // Name : Swetha Rajeev
    // Username: rswetha

    private lateinit var binding: ActivityMainBinding
    private lateinit var scoreActivityLauncher: ActivityResultLauncher<Intent>

    private val vm: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        scoreActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val shouldReset = result.data?.getBooleanExtra(EXTRA_RESET, false) ?: false
                if (shouldReset) {
                    vm.resetAllQuestions()
                }
                vm.currentQuestionIndex = 0
                updateView(true)
            }
        }



        setButtonListeners()
        updateView(true)
    }

    private fun setButtonListeners() {
        listOf(
            binding.answer0Button,
            binding.answer1Button,
            binding.answer2Button,
            binding.answer3Button
        ).forEachIndexed { index, button ->
            button.apply {
                setOnClickListener { selectAnswer(index) }
            }
        }

        binding.hintButton.apply {
            setOnClickListener { applyHint() }
        }


        binding.submitButton.apply {
            setOnClickListener {
                if (vm.isLastQuestion) {
                    val score = calculateScore()
                    scoreActivityLauncher.launch(ScoreActivity.newIntent(this@MainActivity, score))
                } else {
                    submitAnswer()
                    updateView(true)
                }
            }
        }

    }

    private fun selectAnswer(index: Int) {
        vm.currentQuestion.answerList.forEachIndexed { i, answer ->

            if (i == index) {
                answer.isSelected = !answer.isSelected
            } else {
                answer.isSelected = false
            }
        }
        updateView(true)
    }



    private fun applyHint() {
        vm.currentQuestion.answerList
            .filter { !it.isCorrect && it.isEnabled }
            .randomOrNull()?.apply {
                isEnabled = false
                if (isSelected) isSelected = false
            }
        updateView(true)
    }


    private fun submitAnswer() {
        vm.moveToNextQuestion()
        updateView(true)
    }

    private fun updateView(fullUpdate: Boolean = false) {
        if (fullUpdate) {
            binding.questionText.text = getString(vm.currentQuestion.questionResId)

            val buttons = listOf(binding.answer0Button, binding.answer1Button, binding.answer2Button, binding.answer3Button)
            vm.currentQuestion.answerList.zip(buttons).forEach { (answer, button) ->
                button.text = getString(answer.textResId)
                button.isEnabled = answer.isEnabled
                button.isSelected = answer.isSelected
                button.updateColor()
            }
        }

        binding.hintButton.isEnabled = vm.currentQuestion.answerList.count { it.isEnabled && !it.isCorrect } > 0
        binding.submitButton.isEnabled = vm.currentQuestion.answerList.any { it.isSelected && it.isEnabled }
    }





    private fun calculateScore(): Int {
        return vm.questionList.sumOf { question ->
            val correctAnswerPoints = if (question.answerList.any { it.isCorrect && it.isSelected }) 25 else 0
            val hintPenalty = question.answerList.count { !it.isCorrect && !it.isEnabled } * 8
            correctAnswerPoints - hintPenalty
        }
    }



}
