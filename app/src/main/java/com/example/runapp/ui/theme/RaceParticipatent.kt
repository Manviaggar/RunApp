package com.example.runapp.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

class RaceParticipatent
    (
    val name:String,
    val maxProgress:Int=100,
    private val IncrementProgress:Int=1,
   private val initialProgress:Int=0,
    val progressDelayMilli:Long=500L
){
        init {
            require(maxProgress>0){
                "maxprogress=$maxProgress;must be>0"
            }
            require(IncrementProgress>0){
                "Incrementprogress=$IncrementProgress;must be>0"
            }
        }
var currentProgress by mutableStateOf(initialProgress)
    private set
    suspend fun run(){
        while(currentProgress < maxProgress){
            delay(progressDelayMilli)
            currentProgress += IncrementProgress
        }
    }
    fun reset(){
        currentProgress=0
    }

}
val RaceParticipatent.progressFactor: Float
    get() = currentProgress / maxProgress.toFloat()