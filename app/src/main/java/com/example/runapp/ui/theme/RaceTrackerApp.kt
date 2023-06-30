package com.example.runapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.runapp.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
private fun RaceControl(
isRunning:Boolean,
onReset:()->Unit,
onRunStateChange:(Boolean)->Unit,
modifier: Modifier=Modifier
){
Row (
    modifier=Modifier.fillMaxWidth(),
    horizontalArrangement=Arrangement.SpaceEvenly){
    Button(onClick = {onRunStateChange(!isRunning)}) {
        Text(if(isRunning) stringResource(R.string.pause) else stringResource(R.string.start))
    }
    Button(onClick=onReset){
         Text(stringResource(R.string.reset))
    }
}

}
@Composable
private fun StatusIndicator(
    participantName: String,
    currentProgress: Int,
    maxProgress: String,
    progressFactor: Float,
    modifier: Modifier = Modifier
){
Row{
    Text(participantName,Modifier.padding(end=8.dp))
   Column(
       modifier= modifier
           .fillMaxWidth()
           .verticalScroll(rememberScrollState()),
       verticalArrangement = Arrangement.spacedBy(8.dp)
   ){
       LinearProgressIndicator(
           progress = progressFactor,
           modifier = Modifier
               .fillMaxWidth()
               .height(16.dp)
               .clip(RoundedCornerShape(4.dp))
       )
       Row(
           modifier = Modifier.fillMaxWidth(),
           horizontalArrangement = Arrangement.SpaceBetween
       ) {
           Text(
               text = stringResource(R.string.progress_percentage, currentProgress),
               textAlign = TextAlign.Start,
               modifier = Modifier.weight(1f)
           )
           Text(
               text = maxProgress,
               textAlign = TextAlign.End,
               modifier = Modifier.weight(1f)
           )
       }
   }
}
}
@Composable
private fun RaceTrackerScreen(
    playerOne: RaceParticipatent,
    playerTwo: RaceParticipatent,
    isRunning: Boolean,
    onRunStateChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.run),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.runnnn),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
            StatusIndicator(
                participantName = playerOne.name,
                currentProgress = playerOne.currentProgress,
                maxProgress = stringResource(
                    R.string.progress_percentage,
                    playerOne.maxProgress
                ),
                progressFactor = playerOne.progressFactor
            )
            Spacer(modifier = Modifier.size(24.dp))
            StatusIndicator(
                participantName = playerTwo.name,
                currentProgress = playerTwo.currentProgress,
                maxProgress = stringResource(
                    R.string.progress_percentage,
                    playerTwo.maxProgress
                ),
                progressFactor = playerTwo.progressFactor
            )
            RaceControl(
                isRunning = isRunning,
                onRunStateChange = onRunStateChange,
                onReset = {
                    playerOne.reset()
                    playerTwo.reset()
                    onRunStateChange(false)
                }
            )
        }
    }
}

@Composable
fun RaceTrackerApp() {
        val playerOne = remember {
            RaceParticipatent(name = "Player 1", IncrementProgress = 1)
        }
        val playerTwo = remember {
            RaceParticipatent(name = "Player 2", IncrementProgress = 2)
        }
        var raceInProgress by remember { mutableStateOf(false) }

        if (raceInProgress) {
            LaunchedEffect(playerOne, playerTwo) {
                coroutineScope {
                    launch { playerOne.run() }
                    launch { playerTwo.run() }
                }
                raceInProgress = false
            }
        }
        RaceTrackerScreen(
            playerOne = playerOne,
            playerTwo = playerTwo,
            isRunning = raceInProgress,
            onRunStateChange = { raceInProgress = it }
        )
}
