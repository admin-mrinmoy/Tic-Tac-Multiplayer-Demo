package com.mrinmoy.tictacgo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mrinmoy.tictacgo.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playOfflineButton.setOnClickListener {
            createOfflineGame()
        }

        binding.createOnlineGame.setOnClickListener{
            createOnlineGame()
        }

        binding.btnJoinGame.setOnClickListener {
            joinOnlineGame()
        }

    }

    private fun joinOnlineGame() {

        var gameID=binding.gameInput.text.toString()
        if (gameID.isEmpty())
        {
            binding.gameInput.setError("Please enter game id")
            return
        }
        GameData.myID="O"
        Firebase.firestore.collection("games")
            .document(gameID)
            .get()
            .addOnSuccessListener {
                val model=it?.toObject(GameModel::class.java)
                if (model==null)
                {
                    binding.gameInput.setError("Please Enter a valid game id")
                }
                else{
                    model.gameStatus=GameStatus.JOINED
                    GameData.sameGameModel(model)
                    startGame()
                }
            }

    }

    fun createOnlineGame()
    {
        GameData.myID="X"
        GameData.sameGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameID = Random.nextInt(1000..9999).toString()
            )
        )
        startGame()
    }

    fun createOfflineGame() {
        GameData.sameGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )
        startGame()

    }

    fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }
}