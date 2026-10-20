package com.example.geoquiz6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoquiz6.ui.theme.GeoQuiz6Theme
import com.example.geoquiz6.ui.theme.PurplePrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoQuiz6Theme {
                GeoQuizApp()
            }
        }
    }
}

@Composable
fun GeoQuizApp() {
    var currentIndex by remember { mutableIntStateOf(0) }
    var isAnswered by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var showResultDialog by remember { mutableStateOf(false) }

    val currentQuestion = questionList[currentIndex]
    val isLastQuestion = currentIndex == questionList.size - 1

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                GeoQuizTopBar()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = currentQuestion.text,
                        fontSize = 18.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    AnswerButtons(
                        isVisible = !isAnswered,
                        onAnswerSelected = { userChoice ->
                            if (!isAnswered) {
                                isAnswered = true
                                if (userChoice == currentQuestion.answer) {
                                    correctAnswersCount++
                                }
                                if (isLastQuestion) {
                                    showResultDialog = true
                                }
                            }
                        }
                    )
                }
            }

            val isNextVisible = !(isLastQuestion && isAnswered)

            if (isNextVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(bottom = 24.dp, end = 20.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = {
                            if (currentIndex < questionList.size - 1) {
                                currentIndex++
                                isAnswered = false
                            }
                        },
                        enabled = true,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                    ) {
                        Text(
                            text = "NEXT >",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        if (showResultDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(
                        text = "Результат теста",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Вы ответили правильно на $correctAnswersCount из ${questionList.size} вопросов!"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            currentIndex = 0
                            isAnswered = false
                            correctAnswersCount = 0
                            showResultDialog = false
                        }
                    ) {
                        Text(
                            text = "Начать заново",
                            color = PurplePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun AnswerButtons(
    isVisible: Boolean,
    onAnswerSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isVisible) 1f else 0f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { if (isVisible) onAnswerSelected(true) },
            enabled = isVisible,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = "TRUE",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = { if (isVisible) onAnswerSelected(false) },
            enabled = isVisible,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = "FALSE",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GeoQuizTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp)
            .background(PurplePrimary)
            .statusBarsPadding()
            .height(60.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "GeoQuiz",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
