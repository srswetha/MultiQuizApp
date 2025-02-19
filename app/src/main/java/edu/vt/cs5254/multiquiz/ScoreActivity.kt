package edu.vt.cs5254.multiquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.vt.cs5254.multiquiz.databinding.ActivityScoreBinding

private const val EXTRA_SCORE = "edu.vt.cs5254.multiquiz.score"
private const val EXTRA_RESET = "edu.vt.cs5254.multiquiz.reset"

class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding

    // Use ViewModel to track state
    private val scoreViewModel: ScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra(EXTRA_SCORE, -1)


        if (!scoreViewModel.isReset) {
            scoreViewModel.updateScore(score)
        }

        updateView()

        binding.resetButton.setOnClickListener {
            scoreViewModel.resetQuiz()
            updateView()


            setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_RESET, true)
            })

        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(RESULT_OK, Intent().apply {
                    putExtra(EXTRA_RESET, scoreViewModel.isReset)
                })
                finish()
            }
        })
    }

    private fun updateView() {
        binding.scoreText.text = if (scoreViewModel.isReset) getString(R.string.question_mark) else scoreViewModel.score.toString()
        binding.resetButton.isEnabled = scoreViewModel.isResetButtonEnabled
    }


    companion object {
        fun newIntent(context: Context, score: Int): Intent {
            return Intent(context, ScoreActivity::class.java).apply {
                putExtra(EXTRA_SCORE, score)
            }
        }
    }
}