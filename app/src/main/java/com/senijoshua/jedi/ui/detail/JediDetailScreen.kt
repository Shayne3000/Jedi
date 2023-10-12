@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.jedi.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.jedi.R

@Composable
fun JediDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: JediDetailViewModel,
    topBarTitle: String,
    onBackClicked: () -> Unit
) {
    val screenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    JediDetailContent(
        topBarTitle = topBarTitle,
        uiState = screenUiState,
        onErrorMessageShown = { viewModel.errorMessageShown() },
        onBackClicked = { onBackClicked() }
    )
}

@Composable
private fun JediDetailContent(
    modifier: Modifier = Modifier,
    topBarTitle: String = "",
    uiState: JediDetailScreenUiState,
    onErrorMessageShown: () -> Unit,
    onBackClicked: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = topBarTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_icon)
                )
            }
        })
    }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            // TODO handle the various forms/implementations of the UI State
            when (uiState) {
                is JediDetailScreenUiState.Loading -> {
                    // show circular progress indicator
                }

                is JediDetailScreenUiState.Success -> {
                    // load Screen details
                }

                is JediDetailScreenUiState.Error -> {
                    uiState.errorMessage?.let { message ->
                        LaunchedEffect(snackBarHostState) {
                            snackBarHostState.showSnackbar(message = message)
                            onErrorMessageShown()
                        }
                    }
                }
            }
        }
    }
}
