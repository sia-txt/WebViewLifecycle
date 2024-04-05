@file:Suppress("UNUSED_EXPRESSION")

package com.example.webviewlifecycle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

private const val TAG = "MainScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebviewTopBar(viewModel: MainActivityViewModel) {
    TopAppBar(
        modifier = Modifier.padding(10.dp),
        navigationIcon = { Icon(Icons.Default.Close, "", modifier = Modifier.padding(10.dp)) },
        title = { WebviewAddressBar(viewModel) }
    )
}

@Composable
fun TopBarNaviIcon() {
//    TODO("Not yet implemented")
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WebviewAddressBar(viewModel: MainActivityViewModel) {
    var addressBarUrl by remember { mutableStateOf(TextFieldValue(viewModel.url.value.orEmpty())) }
    var focusState by remember { mutableStateOf(false) }
    val httpUrl = viewModel.httpUrl.observeAsState().value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = httpUrl) {
        addressBarUrl = TextFieldValue(httpUrl.orEmpty())
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .onFocusChanged {
                focusState = it.isFocused
                if (it.isFocused) {
                    addressBarUrl =
                        addressBarUrl.copy(selection = TextRange(0, end = addressBarUrl.text.length))
                }
            },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = addressBarUrl,
        onValueChange = {
            addressBarUrl = it
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                viewModel.uiAction.invoke(WebViewUiAction.AddressChanged(addressBarUrl.text))
                keyboardController?.hide()
                focusManager.clearFocus()
                focusState = false
            }
        ),
        singleLine = true,
        trailingIcon = {
            AnimatedVisibility(visible = focusState) {
                Icon(Icons.Default.Clear, "", modifier = Modifier.clickable {
                    addressBarUrl = addressBarUrl.copy(text = "")
                })
            }
        }
    )
}

@Composable
fun LinearDeterminateIndicator(viewModel: MainActivityViewModel) {
    val progress = viewModel.progress.observeAsState().value ?: 0
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = Color.White)
    ) {
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