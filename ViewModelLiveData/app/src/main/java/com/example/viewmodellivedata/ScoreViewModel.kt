package com.example.viewmodellivedata

import androidx.annotation.IntegerRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel: ViewModel() {
     var score: MutableLiveData<Int> = MutableLiveData<Int>()
    fun addScore(){
        if (score != null)
        {
            score.value = score.value?.plus(1)
        }
    }
    @JvmName("getScore1")
    fun getScore(): MutableLiveData<Int>
    {
             if (score.value == null){
                 score.value = 0
             }
        return score
    }
    fun resetScore(){
        score.value = 0
    }
}