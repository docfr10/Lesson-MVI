package com.example.lesson_mvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.lesson_mvi.presentation.NotesScreen
import com.example.lesson_mvi.ui.theme.LessonMVITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            LessonMVITheme {
                Surface {
                    NotesScreen()
                }
            }
        }
    }
}