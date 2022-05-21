package com.jansir.composesudoku.ui.widget

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jansir.composesudoku.ui.widget.menu.cascade.CascadeMenu
import com.jansir.composesudoku.ui.widget.menu.cascade.CascadeMenuItem
import com.jansir.composesudoku.ui.widget.menu.cascade.cascadeMenu

@ExperimentalAnimationApi
@Composable
fun Menu(
    modifier: Modifier = Modifier,
    isOpen: Boolean = false,
    setIsOpen: (Boolean) -> Unit,
    itemSelected: (String) -> Unit,
    offset: DpOffset = DpOffset.Zero,
) {
    val menu = getMenu()
    CascadeMenu(
        modifier = modifier,
        isOpen = isOpen,
        menu = menu,
        onItemSelected = itemSelected,
        onDismiss = { setIsOpen(false) },
        offset = offset,
    )
}

const val MENU_SWITCH_THEMES ="切换主题"
const val MENU_SETTING ="设置"

fun getMenu(): CascadeMenuItem<String> {
    val menu = cascadeMenu<String> {
        item(MENU_SWITCH_THEMES, MENU_SWITCH_THEMES) {
            icon(Icons.TwoTone.Language)
        }
        item(MENU_SETTING, MENU_SETTING) {
            icon(Icons.TwoTone.FileCopy)
        }
/*        item("share", "Share") {
            icon(Icons.TwoTone.Share)
            item("to_clipboard", "To clipboard") {
                item("pdf", "PDF")
                item("epub", "EPUB")
                item("web_page", "Web page")
                item("microsoft_word", "Microsoft word")
            }
            item("as_a_file", "As a file") {
                item("pdf", "PDF")
                item("epub", "EPUB")
                item("web_page", "Web page")
                item("microsoft_word", "Microsoft word")
            }
        }
        item("remove", "Remove") {
            icon(Icons.TwoTone.DeleteSweep)
            item("yep", "Yep") {
                icon(Icons.TwoTone.Done)
            }
            item("go_back", "Go back") {
                icon(Icons.TwoTone.Close)
            }
        }*/
    }
    return menu
}