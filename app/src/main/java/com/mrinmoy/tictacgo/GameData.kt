package com.mrinmoy.tictacgo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object GameData {

    private var _gameModel:MutableLiveData<GameModel> = MutableLiveData()
    var gameModel:LiveData<GameModel> = _gameModel
    var myID=""

    fun sameGameModel(model:GameModel)
    {
        _gameModel.postValue(model)

        if (model.gameID!="-1") {
            Firebase.firestore.collection("games")
                .document(model.gameID)
                .set(model)
        }

    }

    fun fetchGameModel()
    {
        gameModel.value?.apply {
            if (gameID!="-1")
            {
                Firebase.firestore.collection("games")
                    .document(gameID)
                    .addSnapshotListener { value, error ->
                        val model=value?.toObject(GameModel::class.java)
                        _gameModel.postValue(model)
                    }
            }
        }
    }

}