package com.jansir.composesudoku

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jansir.composesudoku.ui.main.*
import com.jansir.composesudoku.ui.theme.*


class MainActivity : ComponentActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContent {
            ComposeSudokuTheme {
                SudokuApp()
            }
        }
    }
}

