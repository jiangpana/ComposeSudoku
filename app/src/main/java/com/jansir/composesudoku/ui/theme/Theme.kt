package com.jansir.composesudoku.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Stable
class AppColors(
    textPrimary: Color,
    textSecondary: Color,
    themeUi: Color,
    sudoku_view_bg: Color,
    sudokuview_text_color: Color,
    sudokuview_text_color_uneditable: Color,
) {
    var textPrimary: Color by mutableStateOf(textPrimary)
    var textSecondary: Color by mutableStateOf(textSecondary)
    var themeUi: Color by mutableStateOf(themeUi)
    var sudoku_view_bg: Color by mutableStateOf(sudoku_view_bg)
    var sudokuview_text_color: Color by mutableStateOf(sudokuview_text_color)
    var sudokuview_text_color_uneditable: Color by mutableStateOf(sudokuview_text_color_uneditable)
}

private val BlueColorPalette = AppColors(
    textPrimary = Purple200,
    textSecondary = Purple200,
    themeUi = Purple200,
    sudoku_view_bg = Color.White,
    sudokuview_text_color = Color(0xff1D62BF),
    sudokuview_text_color_uneditable = Color.Black,

    )
private val DarkColorPalette = AppColors(
    textPrimary = Purple500,
    textSecondary = Purple500,
    themeUi = Purple500,
    sudoku_view_bg = Color(0xff242627),
    sudokuview_text_color =Color(0xffB7EBFF),
    sudokuview_text_color_uneditable =Color(0xffAFAFAF),
    )

@Composable
fun ComposeSudokuTheme(
    theme: AppTheme.Theme = appThemeState.value,
    content: @Composable () -> Unit
) {
    val targetColors = when (theme) {
        AppTheme.Theme.Blue -> {
            BlueColorPalette
        }
        AppTheme.Theme.Dark -> DarkColorPalette
    }

    val appColors = AppColors(
        textPrimary = animateColorAsState(targetColors.textPrimary, TweenSpec(600)).value,
        textSecondary = animateColorAsState(targetColors.textSecondary, TweenSpec(600)).value,
        themeUi = animateColorAsState(targetColors.themeUi, TweenSpec(600)).value,
        sudoku_view_bg = animateColorAsState(targetColors.sudoku_view_bg, TweenSpec(600)).value,
        sudokuview_text_color =  animateColorAsState(targetColors.sudokuview_text_color, TweenSpec(600)).value,
        sudokuview_text_color_uneditable =  animateColorAsState(targetColors.sudokuview_text_color_uneditable, TweenSpec(600)).value,
    )

    val systemUiCtrl = rememberSystemUiController()
    systemUiCtrl.setStatusBarColor(appColors.themeUi)
    systemUiCtrl.setNavigationBarColor(appColors.themeUi)
    systemUiCtrl.setSystemBarsColor(appColors.themeUi)

    ProvideWindowInsets {
        CompositionLocalProvider(LocalAppColors provides appColors, content = content)
    }
}
var LocalAppColors = compositionLocalOf {
    BlueColorPalette
}

@Stable
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    enum class Theme {
        Blue, Dark
    }
}

val appThemeState: MutableState<AppTheme.Theme> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    mutableStateOf(AppTheme.Theme.Blue)
}


