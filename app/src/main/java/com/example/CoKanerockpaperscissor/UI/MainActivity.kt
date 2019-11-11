package com.example.CoKanerockpaperscissor.UI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.CoKanerockpaperscissor.R
import com.example.CoKanerockpaperscissor.database.GameRepository
import com.example.CoKanerockpaperscissor.model.Game
import com.example.CoKanerockpaperscissor.model.Gesture

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1

    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        gameRepository = GameRepository(this)
        initViews()
    }


    private fun initViews() {
        initListeners()
        updateStats()
    }


    private fun updateStats() {
        CoroutineScope(Dispatchers.Main).launch {
            val wins = withContext(Dispatchers.IO) {
                gameRepository.getWins()
            }
            val draws = withContext(Dispatchers.IO) {
                gameRepository.getDraws()
            }
            val losses = withContext(Dispatchers.IO) {
                gameRepository.getLosses()
            }
            tvStats.text = getString(
                R.string.win_draw_lose,
                wins.toString(),
                draws.toString(),
                losses.toString()
            )
        }
    }


    private fun initListeners() {
        ibtnHistory.setOnClickListener {
            startActivity()
        }
        Rock.setOnClickListener { handleGame(Gesture.ROCK) }
        Paper.setOnClickListener { handleGame(Gesture.PAPER) }
        Scissor.setOnClickListener { handleGame(Gesture.SCISSOR) }
    }


    private fun startActivity() {
        val intent = Intent(this, GameHistoryActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }


    private fun handleGame(gesture: Gesture) {
        val computerAction = (0..3).shuffled().first()
        val computerGesture = assignGesture(computerAction)
        var game = Game(
            null,
            "",
            Date(),
            gesture,
            computerGesture
        )

        game = calculateWinner(game)
        insertGameIntoDatabase(game)
        displayGameResults(game)
        updateStats()
    }


    private fun insertGameIntoDatabase(game: Game) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }
    }


    private fun assignGesture(value: Int): Gesture {
        return when (value) {
            0 -> Gesture.ROCK
            1 -> Gesture.PAPER
            3 -> Gesture.SCISSOR
            else -> Gesture.ROCK
        }
    }


    private fun calculateWinner(game: Game): Game {
        game.result = when {
            game.computerAction == game.userAction -> getString(R.string.draw)
            game.computerAction == Gesture.ROCK && game.userAction == Gesture.SCISSOR -> getString(
                R.string.computer_win
            )
            game.computerAction == Gesture.SCISSOR && game.userAction == Gesture.PAPER -> getString(
                R.string.computer_win
            )
            game.computerAction == Gesture.PAPER && game.userAction == Gesture.ROCK -> getString(
                R.string.computer_win
            )
            else -> getString(R.string.you_win)
        }
        return game
    }


    private fun displayGameResults(game: Game) {
        tvResult.text = game.result
        Computer.setImageDrawable(getDrawable(game.computerAction.drawableId))
        You.setImageDrawable(getDrawable(game.userAction.drawableId))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateStats()
        resetGameResults()
    }


    private fun resetGameResults() {
        tvResult.text = getString(R.string.initial_result)
        Computer.setImageDrawable(getDrawable(Gesture.PAPER.drawableId))
        You.setImageDrawable(getDrawable(Gesture.PAPER.drawableId))
    }
}
