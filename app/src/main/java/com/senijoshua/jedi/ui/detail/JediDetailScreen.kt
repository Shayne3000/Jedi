@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.jedi.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.jedi.R
import com.senijoshua.jedi.data.Jedi
import com.senijoshua.jedi.ui.components.JediCircularProgressIndicator
import com.senijoshua.jedi.ui.theme.JediTheme
import com.senijoshua.jedi.ui.util.JediPreview

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
    onErrorMessageShown: () -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = topBarTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { onBackClicked() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_icon),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // TODO handle the various forms/implementations of the UI State
            when (uiState) {
                is JediDetailScreenUiState.Loading -> {
                    JediCircularProgressIndicator(modifier)
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

@JediPreview
@Composable
private fun JediDetailPreview() {
    val previewScreenUiState = JediDetailScreenUiState.Success(
        Jedi(
            1, "Luke Skywalker", "male", "170cm", "81kg", "Brown", "Green"
        )
    )

    JediTheme {
        JediDetailContent(topBarTitle = "Luke Skywalker", uiState = JediDetailScreenUiState.Error("test"))
    }
}
