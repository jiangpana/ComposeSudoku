先上实现效果图

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/caf813484a754e2593c20cff5a78cc04~tplv-k3u1fbpfcp-zoom-1.image)

横屏界面

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/3adfc8c8ba56470997e060bf1e4a6a5e~tplv-k3u1fbpfcp-zoom-1.image)

点右上角菜单一键切换主题 , 黑色主题颜色没调(随便弄的颜色) , 以后可能回调

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/be016783456f4ff6952263c2022c1508~tplv-k3u1fbpfcp-zoom-1.image)

效果图放完了就开讲

### 准备游戏题目和答案

就在网上随便弄道题目

```kotlin
    val ques = "000078962763429185928561374284196753396745218157832496831257649672984531549613827"
    val answ = "415378962763429185928561374284196753396745218157832496831257649672984531549613827"
```

初始化游戏题目成二维数组格式

我们先弄9*9 格式的数独 , 每个Cell代表数组的每一格 , 9*9数独有81个Cell ,

然后把每个Cell对应的 行列和宫数据都初始化好

```kotlin
 fun getQuesArray2d(ques: String = Constant.ques): Array<Array<Cell>> {
        val ans = Constant.answ
        var index = 0
        val sectors =
            arrayOfNulls<CellGroup>(9)
        val rows =
            arrayOfNulls<CellGroup>(9)
        val cols =
            arrayOfNulls<CellGroup>(9)
        for (i in 0 until 9) {
            sectors[i] = CellGroup()
            rows[i] = CellGroup()
            cols[i] = CellGroup()
        }
        val array2d = Array(9) { rowIndex ->
            Array(9) { colIndex ->
                val cellVal = ques[index].toString().toInt()
                val ansVal = ans[index++].toString().toInt()
                Cell(
                    rowIndex = rowIndex,
                    colIndex = colIndex,
                    value = cellVal,
                    ansValue = ansVal,
                    isEditable = cellVal == 0
                ).apply {
                    row = rows[rowIndex]!!
                    col = cols[colIndex]!!
                    sector = sectors[((colIndex / 3) * 3) + (rowIndex / 3)]!!
                    row?.addCell(this)
                    col?.addCell(this)
                    sector?.addCell(this)
                }
            }
        }
        return array2d
    }
```

### 处理绘制

数据准备并初始化好就开始draw了 , 要绘制的有高亮的行列宫格 , 相似Cell , Cell , 网格线

贴一下draw网格线的代码吧 , 用到的主要是**drawLine** 方法

drawline很简单 , 两点确定一点直线 , 计算得到开始点和结束点就行了

```kotlin
private fun DrawScope.drawLine(
    width: Float
) {
    //画9x9垂直线
    for (c in 0..9) {
        val start = Offset(c * cellWidth, 0f)
        val end = Offset(c * cellWidth, width)
        drawLine(Color.Black, start, end, strokeWidth = 2.dp.value)
    }

    //画9x9水平线
    for (c in 0..9) {
        val start = Offset(0f, c * cellWidth)
        val end = Offset(width, c * cellWidth)
        drawLine(Color.Black, start, end, strokeWidth = 2.dp.value)
    }
    //画3x3 垂直粗线
    for (c in 0..3) {
        val start = Offset(c * cellWidth * 3, 0f)
        val end = Offset(c * cellWidth * 3, width)
        drawLine(Color.Black, start, end, strokeWidth = 8.dp.value, cap = StrokeCap.Square)
    }

    //画3x3 水平粗线
    for (c in 0..3) {
        val start = Offset(0f, c * cellWidth * 3)
        val end = Offset(width, c * cellWidth * 3)
        drawLine(Color.Black, start, end, strokeWidth = 8.dp.value, cap = StrokeCap.Square)
    }
}
```

### 处理交互

Modifier 有个pointerInteropFilter , 触摸事件都可以在这个方法监听到

```kotlin
  Canvas(
            modifier = Modifier
                .padding(start = sudoku_view_padding, end = sudoku_view_padding)
                .fillMaxSize()
                .background(AppTheme.colors.sudoku_view_bg)
                .pointerInteropFilter {
                    return@pointerInteropFilter handleMotionEvent(it) { row, col ->
                        viewModel.dispatch(SudokuOperateAction.MOVE(row, col))
                    }
                }
        )
```

通过 x 和 y值得到当前触摸点的 rowIndex 和 colIndex

```kotlin
fun handleMotionEvent(it: MotionEvent, onTouch: (Int, Int) -> Unit): Boolean {
    val row = (it.y / cellWidth).toInt()
    val col = (it.x / cellWidth).toInt()
    onTouch(row, col)
    return true
}
```

compose触摸事件和原生的view事件传递机制不一样 , compose触摸区域是整个屏幕区域而不是当前draw区域 , 所以就直接try一下吧 就不做边界判断了

```kotlin
    fun dispatch(action: SudokuOperateAction) {
        when (action) {
            is SudokuOperateAction.MOVE -> {
                try {
                    viewStates = viewStates.copy(selectCell = viewStates.cells!![action.row][action.col])
                } catch (e: Exception) {
                }
            }
            ....
       }
```

viewStates = viewStates.copy 会重新 刷新界面 调用draw方法 , 但是点击按钮 就不用给viewStates 重新赋值了 , 因为点击按钮 界面也会刷新 , 没必要在刷新一次

```kotlin
            is SudokuOperateAction.CHANGE_VALUE -> {
                //text click 会触发 SudokuView Canvas Draw
                // 所以不用 viewStates = viewStates.copy(selectCell = copyCell) 再去触发一次了
                if (!viewStates.selectCell.isEditable) return
                if (viewStates.selectCell.value == action.value){
                    action.value = 0
                }
                val selectCell =viewStates.selectCell
                selectCell.value =action.value
                viewStates.cells!![selectCell.rowIndex][selectCell.colIndex] .value=  action.value
                if (selectCell.value!=0){
                    viewModelScope.launch {
                        _viewEvents.send(SudokuOperateAction.DO_ANIMATION)
                    }
                }
                checkSuccess()
            }
```

### 处理横竖屏

先把判断横竖屏提取出一个方法来 , 很多地方都会用到

```kotlin
@Composable
fun isPortrait():Boolean{
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}
```

##### 游戏界面横竖屏

竖屏的时候就以宽作为游戏的宽高 , 横屏的时候就以高的0.9倍作为游戏宽高

```kotlin
    val screenWidth= if (isPortrait()){
        configuration.screenWidthDp.dp
    }else {
        configuration.screenHeightDp.dp.times(0.9f)
    }
    Box(
        modifier = modifier
            .width(screenWidth)
            .height(screenWidth)
    ) {
        Canvas(){
        ......}
        }
```

### 填入数字动画

用最基础的Animatable 来实现一个数字从0 放到sudoku_text_size 动画

```kotlin
    val textAnimate = remember { Animatable(sudoku_text_size.toPx()) }
    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is SudokuOperateAction.DO_ANIMATION){
                if (!textAnimate.isRunning){
                    textAnimate.stop()
                    textAnimate.snapTo(0f)
                    textAnimate.animateTo(sudoku_text_size.toPx(),
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    )
                }
            }
        }
    }
```

### 后续待开发

-   游戏设置界面
-   数据持久化
-   行列宫格 , 开局动画

项目地址 : <https://github.com/jiangpana/ComposeSudoku
