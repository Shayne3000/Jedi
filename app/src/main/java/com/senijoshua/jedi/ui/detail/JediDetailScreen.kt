package com.senijoshua.jedi.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun JediDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: JediDetailViewModel,
    topBarTitle: String = "",
    onBackClicked: () -> Unit
) {
    val screenUiState by
    JediDetailContent()
}

@Composable
private fun JediDetailContent() {
    // TODO Add a Scaffold that holds the AppBar and the rest of the screen content i.e. Jedi
}
