package com.jansir.composesudoku.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jansir.composesudoku.R
import com.jansir.composesudoku.ext.isPortrait
import com.jansir.composesudoku.ext.open
import com.jansir.composesudoku.ui.setting.GameSettingActivity
import com.jansir.composesudoku.ui.theme.*
import com.jansir.composesudoku.ui.widget.MENU_SETTING
import com.jansir.composesudoku.ui.widget.MENU_SWITCH_THEMES
import com.jansir.composesudoku.ui.widget.Menu
import com.jansir.composesudoku.ui.widget.SampleAlertDialog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SudokuApp(viewModel: SudokuViewModel = viewModel()) {
    val (isOpen, setIsOpen) = remember { mutableStateOf(false) }
    val timerData by viewModel.timerState.collectAsState()
    val channel = remember { Channel<String>(Channel.BUFFERED) }

    val ctx = LocalContext.current
    LaunchedEffect(Unit) {
        channel.receiveAsFlow().collect {
            if (it == MENU_SWITCH_THEMES) {
                if (appThemeState.value == AppTheme.Theme.Dark) {
                    appThemeState.value = AppTheme.Theme.Blue
                } else {
                    appThemeState.value = AppTheme.Theme.Dark
                }
            }
            if (it == MENU_SETTING) {
                ctx.open<GameSettingActivity>()
            }
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { setIsOpen(true) }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.TwoTone.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            )
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            Box {
                Menu(
                    isOpen = isOpen,
                    setIsOpen = setIsOpen,
                    offset = DpOffset(2000.dp, (-2000).dp),
                    itemSelected = {
                        channel.trySend(it)
                        setIsOpen(false)
                    })
                HomeScreenContent(viewModel, timerData)
            }
        }
    }

}

@Composable
private fun HomeScreenContent(
    viewModel: SudokuViewModel,
    timerData: TimerData
) {
    if (viewModel.viewStates.gameSuccess) {
        SampleAlertDialog(
            title = stringResource(R.string.game_success),
            content = stringResource(R.string.restart_the_game),
            onConfirmClick = {
                viewModel.dispatch(SudokuOperateAction.RE_GAME)
            },
            onDismiss = {
                viewModel.dispatch(SudokuOperateAction.DISMISS_DIALOG)
            }
        )
    }

    if (isPortrait()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            SudokuTimerView(
                type = TimerType.HH_MM_SS,
                timerData = timerData,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp, bottom = 4.dp)
            )
            SudokuGameView(viewModel = viewModel)
            SudokuIMControlView(viewModel)
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize(),
        ) {
            SudokuGameView(viewModel = viewModel)
            SudokuIMControlView(viewModel)
        }

    }

}


/*
                   Modifier.align(Alignment.Start).padding(start = 20.dp , bottom = 4.dp)
                   .border(width = 1.dp, color = green700, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                componentModifier = Modifier
                    .background(color = blue200, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                digitStyle = MaterialTheme.typography.body1.copy(color = orange700),
                separatorModifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                separatorStyle = MaterialTheme.typography.body1.copy(color = teal200)*/
