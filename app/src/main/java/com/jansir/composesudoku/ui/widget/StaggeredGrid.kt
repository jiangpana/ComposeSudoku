package com.jansir.composesudoku.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        // 布局高度需要根据子元素的行数进行计算
        var layoutHeight = 0
        var sumWidth = 0
        var lineMaxHeight = 0
        // 不要进一步限制子元素，用给定的约束测量它们测量的子视图列表
        val placeables = measurables.mapIndexed { index, measurable ->
            // 测量每个子元素
            val placeable = measurable.measure(constraints)
            // 累加行中子元素的宽度, 当宽度大于允许的最大宽度时换行
            sumWidth += placeable.width
            if (sumWidth > constraints.maxWidth) {
                // 如果width大于允许的最大宽度, 就另起一行
                sumWidth = placeable.width
                // 每行最高的子元素累加的高度就是布局高度
                layoutHeight += lineMaxHeight
                // 重置行中最大子元素高度
                lineMaxHeight = 0
            }
            // 记录每行最高的那个子元素高度
            lineMaxHeight = lineMaxHeight.coerceAtLeast(placeable.height)
            placeable
        }
        //当循环结束时加上最后一行的高度
        layoutHeight += lineMaxHeight

        //布局的高度根据子元素占用进行设置, 宽度以约束条件允许的最大宽度
        layout(constraints.maxWidth, layoutHeight) {
            // 跟踪放置子元素的 x 坐标
            var xPosition = 0
            // 跟踪放置子元素的 y 坐标
            var yPosition = 0
            // 将子元素放置在父布局中
            placeables.forEachIndexed { index, placeable ->
                // 在屏幕上定位项目
                placeable.placeRelative(x = xPosition, y = yPosition)
                // 记录放置到的 x 坐标
                xPosition += placeable.width
                if (index < placeables.size && xPosition + placeables[index].width > constraints.maxWidth) {
                    //如果当前行的子元素总宽度大于屏幕宽度就另起一行(注意加上将要放置的子元素宽度进行判断)
                    xPosition = 0
                    // 记录放置到的 y 坐标
                    yPosition += placeable.height
                }
            }
        }
    }
}
