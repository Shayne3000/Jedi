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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstraintLayout
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

    // NB: The approach of putting the scaffold here as shown in the Compose samples
    // i.e. Todoapp/taskdetail works better when the ScreenUIState is a data class and not
    // a sealed class/interface. The Latter is useful when within the screen content composable.

    JediDetailContent(
        modifier = modifier,
        topBarTitle = topBarTitle,
        uiState = screenUiState,
        onErrorMessageShown = { viewModel.errorMessageShown() },
        onBackClicked = { onBackClicked() }
    )

    // Like with AndroidViews, the DB lookup can be called before you start listening for (emitted) screen UI
    // State updates as opposed to doing so afterwards as done here. Though a race happening between them might be of concern,
    // the async call should be main safe and thus will not block the execution of the main thread so collect
    // will be called before the async op returns.
    viewModel.getJedi()
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
            when (uiState) {
                is JediDetailScreenUiState.Loading -> {
                    JediCircularProgressIndicator(modifier)
                }

                is JediDetailScreenUiState.Success -> {
                    val jedi = uiState.jedi

                    ConstraintLayout(
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        val (gender, height, mass, hairColor, skinColor) = createRefs()
                        val verticalPadding = dimensionResource(id = R.dimen.vertical_padding)
                        val horizontalPadding = dimensionResource(id = R.dimen.horizontal_padding)

                        val startGuideline = createGuidelineFromStart(horizontalPadding)

                        Text(
                            modifier = Modifier.constrainAs(gender) {
                                top.linkTo(parent.top, margin = verticalPadding)
                                start.linkTo(startGuideline)
                            },
                            text = annotateText(
                                stringResource(id = R.string.gender, jedi.gender),
                                jedi.gender
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            modifier = Modifier.constrainAs(height) {
                                top.linkTo(gender.bottom, margin = verticalPadding)
                                start.linkTo(startGuideline)
                            },
                            text = annotateText(
                                stringResource(id = R.string.height, jedi.height),
                                jedi.height
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            modifier = Modifier.constrainAs(mass) {
                                top.linkTo(height.bottom, margin = verticalPadding)
                                start.linkTo(startGuideline)
                            },
                            text = annotateText(
                                stringResource(id = R.string.mass, jedi.mass),
                                jedi.mass
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            modifier = Modifier.constrainAs(hairColor) {
                                top.linkTo(mass.bottom, margin = verticalPadding)
                                start.linkTo(startGuideline)
                            },
                            text = annotateText(
                                stringResource(id = R.string.hair_color, jedi.hairColor),
                                jedi.hairColor
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            modifier = Modifier.constrainAs(skinColor) {
                                top.linkTo(hairColor.bottom, margin = verticalPadding)
                                start.linkTo(startGuideline)
                            },
                            text = annotateText(
                                stringResource(id = R.string.skin_color, jedi.skinColor),
                                jedi.skinColor
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
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

private fun annotateText(textToAnnotate: String, textToNotAnnotate: String): AnnotatedString {
    val endIndex = textToAnnotate.indexOf(textToNotAnnotate) - 1

    return buildAnnotatedString {
        append(textToAnnotate)
        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = 0, end = endIndex)
    }
}

@JediPreview
@Composable
private fun JediDetailPreview() {
    val previewScreenUiState = JediDetailScreenUiState.Success(
        Jedi(
            1, "Luke Skywalker", "Male", "170cm", "81kg", "Brown", "Green"
        )
    )

    JediTheme {
        JediDetailContent(
            topBarTitle = "Luke Skywalker",
            uiState = previewScreenUiState
        )
    }
}
