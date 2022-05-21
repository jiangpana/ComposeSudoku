package com.jansir.composesudoku.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import com.jansir.composesudoku.MainActivity.Companion.context
import com.jansir.composesudoku.R
import java.io.Serializable
import java.util.*


fun Dp.toPx(): Float {
    val scale = context.resources.displayMetrics.density
    return  (value * scale + 0.5f);
}

fun Long.millis2time():String{
    val totalSeconds: Long = this / 1000
    val seconds = totalSeconds % 60
    val minutes = totalSeconds / 60 % 60
    val hours = totalSeconds / 3600
    return Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString()

}

inline fun <reified TARGET : Activity> Context.open(
    vararg params: Pair<String, Any>
) = startActivity(Intent(this, TARGET::class.java).putExtras(*params))


fun Intent.putExtras(vararg params: Pair<String, Any>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is String -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putExtra(key, value as Array<String?>)
                    value.isArrayOf<Parcelable>() ->
                        putExtra(key, value as Array<Parcelable?>)
                    value.isArrayOf<CharSequence>() ->
                        putExtra(key, value as Array<CharSequence?>)
                    else -> putExtra(key, value)
                }
            }
            is Serializable -> putExtra(key, value)
        }
    }
    return this
}

@Composable
fun isPortrait():Boolean{
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}