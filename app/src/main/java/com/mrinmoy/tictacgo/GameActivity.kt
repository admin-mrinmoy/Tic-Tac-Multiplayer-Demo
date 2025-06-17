package com.mrinmoy.tictacgo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mrinmoy.tictacgo.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityGameBinding

    private var gameModel: GameModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GameData.fetchGameModel()

        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)

        binding.startGameButton.setOnClickListener {
            startGame()
        }

        GameData.gameModel.observe(this)
        {
            gameModel = it
            setUI()
        }

    }

    fun setUI() {
        gameModel?.apply {
            binding.btn0.text = filledPosition[0]
            binding.btn1.text = filledPosition[1]
            binding.btn2.text = filledPosition[2]
            binding.btn3.text = filledPosition[3]
            binding.btn4.text = filledPosition[4]
            binding.btn5.text = filledPosition[5]
            binding.btn6.text = filledPosition[6]
            binding.btn7.text = filledPosition[7]
            binding.btn8.text = filledPosition[8]

            binding.startGameButton.visibility=View.VISIBLE

            binding.gameStatusText.text =
                when (gameStatus) {
                    GameStatus.CREATED -> {
                        binding.startGameButton.visibility=View.INVISIBLE
                        "Game ID : " + gameID

                    }
                    GameStatus.JOINED -> {
                        "Click On Start Game"
                    }
                    GameStatus.INPROGRESSED -> {
                        binding.startGameButton.visibility=View.INVISIBLE
                        when(GameData.myID){
                            currentPlayer->"Your Turn"
                            else-> currentPlayer + "Turn"
                        }
                    }
                    GameStatus.FINISHED -> {
                        if (winner.isNotEmpty()) {
                            when(GameData.myID)
                            {
                                winner->"You Won"
                                else->winner + "Win"
                            }

                        }
                        else "Draw"
                    }
                }
        }
    }

    fun startGame() {
        gameModel?.apply {
            updateGameData(
                GameModel(
                    gameID = gameID,
                    gameStatus = GameStatus.INPROGRESSED
                )
            )
        }
    }

    fun updateGameData(model: GameModel) {
        GameData.sameGameModel(model)
    }

    fun checkForWinner()
    {
        val winningPosition = arrayOf(
            intArrayOf(0, 1, 2), // Row 1
            intArrayOf(3, 4, 5), // Row 2
            intArrayOf(6, 7, 8), // Row 3
            intArrayOf(0, 3, 6), // Column 1
            intArrayOf(1, 4, 7), // Column 2
            intArrayOf(2, 5, 8), // Column 3
            intArrayOf(0, 4, 8), // Diagonal from top-left
            intArrayOf(2, 4, 6)  // Diagonal from top-right
        )
        gameModel?.apply {
            for(i in winningPosition)
            {
                if (
                    filledPosition[i[0]]==filledPosition[i[1]]
                    && filledPosition[i[1]]==filledPosition[i[2]]
                    && filledPosition[i[0]].isNotEmpty()
                )
                {
                    gameStatus=GameStatus.FINISHED
                    winner=filledPosition[i[0]]
                }
            }
            if(filledPosition.none(){it.isEmpty()})
            {
                gameStatus=GameStatus.FINISHED
            }
            updateGameData(this)
        }
    }

    override fun onClick(v: View?) {
        gameModel?.apply {
            if (gameStatus != GameStatus.INPROGRESSED) {
                Toast.makeText(applicationContext, "Game Not Started", Toast.LENGTH_SHORT).show()
                return
            }

            if (gameID!="-1" && currentPlayer!=GameData.myID)
            {
                Toast.makeText(applicationContext, "Not Your Turn", Toast.LENGTH_SHORT).show()
                return
            }

            val clickPosition=(v?.tag as String).toInt()

            if (filledPosition[clickPosition].isEmpty())
            {
                Log.d("TICTACGO",gameModel.toString());
                filledPosition[clickPosition]=currentPlayer
                currentPlayer=if (currentPlayer=="X") "O" else "X"
                checkForWinner()
                updateGameData(this)
            }
        }


    }
}