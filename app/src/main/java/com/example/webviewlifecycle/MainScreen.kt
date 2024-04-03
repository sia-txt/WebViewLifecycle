package com.example.webviewlifecycle

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebviewTopBar(viewModel: MainActivityViewModel) {
    TopAppBar(
        navigationIcon = { TopBarNaviIcon() },
        title = { WebviewAddressBar(viewModel) },
        actions = {
            // 카카오톡으로 공유
            // 둥둥이로 변환
        }
    )
}

@Composable
fun TopBarNaviIcon() {
//    TODO("Not yet implemented")
}

@Composable
fun WebviewAddressBar(viewModel: MainActivityViewModel) {
    var url by remember { mutableStateOf("") }

    TextField(
        value = url,
        onValueChange = { url = it },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                viewModel.uiAction.invoke(WebViewUiAction.AddressChanged(url))
            }
        ),
        singleLine = true
    )
}

@Composable
fun LinearDeterminateIndicator(viewModel: MainActivityViewModel) {

    val progress = viewModel.progress.observeAsState().value ?: 0
    Log.e("Linear", "LinearDeterminateIndicator: $progress")
    if (progress != 100) {
        LinearProgressIndicator(
            progress = progress.toFloat() / 100,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}

@Composable
fun WebviewBottomBar(viewModel: MainActivityViewModel) {
    BottomAppBar(
    ) {
        // 이게 왜 균등배치가 안될까... TODO
        val modifier = Modifier.weight(1f)
        //TODO: viewModel 직접 참조하지 말고 호이스팅으로 변경
        BottomBarButton(
            onPressed = { viewModel.uiAction.invoke(WebViewUiAction.HistoryBack) },
            modifier = modifier,
            icon = Icons.Default.KeyboardArrowLeft
        )
        BottomBarButton(
            onPressed = { viewModel.uiAction.invoke(WebViewUiAction.HistoryForward) },
            modifier = modifier,
            icon = Icons.Default.KeyboardArrowRight
        )
        BottomBarButton(
            onPressed = { viewModel.uiAction.invoke(WebViewUiAction.RefreshPressed) },
            modifier = modifier,
            icon = Icons.Default.Refresh
        )
    }
}

@Composable
fun BottomBarButton(onPressed: () -> Unit, modifier: Modifier, icon: ImageVector) {
    IconButton(
        onClick = { onPressed() },
        modifier = modifier
    ) {
        Icon(icon, contentDescription = "")
    }
}
