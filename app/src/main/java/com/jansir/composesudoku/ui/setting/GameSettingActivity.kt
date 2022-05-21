package com.jansir.composesudoku.ui.setting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.jansir.composesudoku.MainActivity
import com.jansir.composesudoku.ui.theme.ComposeSudokuTheme

class GameSettingActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.context = applicationContext
        setContent {
            ComposeSudokuTheme {
                Text(text = "GameSettingActivity")
            }
        }
    }
}