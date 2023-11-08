@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.jedi.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.jedi.R
import com.senijoshua.jedi.ui.model.Jedi
import com.senijoshua.jedi.ui.model.fakeJediList
import com.senijoshua.jedi.ui.components.JediCircularProgressIndicator
import com.senijoshua.jedi.ui.theme.JediTheme
import com.senijoshua.jedi.ui.util.JediPreview

const val JEDI_ITEM_CARD_TAG = "JediItemElevatedCard"
const val JEDI_ITEM_COLUMN_IN_CARD_TAG = "JediItemColumnInCard"
const val JEDI_LIST_TAG = "JediListTag"

/**
 * Screen for the Jedi List that exposes an event to navigate to the detail screen
 * to the event handler i.e. caller.
 */
@Composable
fun JediListScreen(
    modifier: Modifier = Modifier,
    viewModel: JediListViewModel,
    onNavigateToJediDetail: (Int, String) -> Unit = {_, _ -> }
) {
    // Start listening for and getting emitted state values i.e. state updates from the
    // UI logic state holder i.e. ViewModel in a lifecycle-aware manner through
    // the uiState StateFlow and convert to Compose State.
    val screenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    JediListContent(
        modifier = modifier,
        uiState = screenUiState,
        onNavigateToJediDetail = { id, name ->
            onNavigateToJediDetail(id, name)
        },
        onErrorMessageShown = {
            viewModel.errorMessageShown()
        }
    )

    viewModel.loadJedis()
}

@Composable
fun JediListContent(
    modifier: Modifier = Modifier,
    uiState: JediListScreenUiState,
    onNavigateToJediDetail: (Int, String) -> Unit = { _, _ -> },
    onErrorMessageShown: () -> Unit = {}
) {
    // Hoist the (snack bar host) state outside the Scaffold and store it in the composition.
    val snackBarHostState = remember { SnackbarHostState() }

    uiState.errorMessage?.let { message ->
        LaunchedEffect(snackBarHostState) {
            snackBarHostState.showSnackbar(message = message)
            onErrorMessageShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.jedi_list_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Screen layout
            Text(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.horizontal_padding),
                        top = dimensionResource(
                            id = R.dimen.vertical_padding,
                        )
                    )
                    .fillMaxWidth(),
                text = stringResource(id = R.string.jedi_list_screen_header),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (uiState.isLoadingJedis) {
                JediCircularProgressIndicator(modifier)
            } else if (uiState.jedis.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize().testTag(JEDI_LIST_TAG)) {
                    items(items = uiState.jedis, key = { jedi -> jedi.id }) {
                        JediItem(jedi = it, onJediClicked = { jediId, jediName ->
                            onNavigateToJediDetail(jediId, jediName)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun JediItem(
    modifier: Modifier = Modifier,
    jedi: Jedi,
    onJediClicked: (Int, String) -> Unit = { _, _ -> }
) {
    ElevatedCard(
        modifier = modifier
            .padding(
                vertical = dimensionResource(id = R.dimen.list_item_card_vertical_padding),
                horizontal = dimensionResource(id = R.dimen.horizontal_padding)
            )
            .height(dimensionResource(id = R.dimen.list_item_height))
            .fillMaxWidth()
            .testTag(JEDI_ITEM_CARD_TAG)
        ,
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .clickable { onJediClicked(jedi.id, jedi.name) }
            .testTag(JEDI_ITEM_COLUMN_IN_CARD_TAG)
        ) {
            Text(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.list_item_vertical_padding),
                    start = dimensionResource(
                        id = R.dimen.horizontal_padding
                    )
                ),
                text = jedi.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Text(
                modifier = Modifier.padding(
                    start = dimensionResource(
                        id = R.dimen.horizontal_padding
                    )
                ),
                text = jedi.gender,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

private val jediListPreviewUiState = JediListScreenUiState(
    jedis = fakeJediList
)

@JediPreview
@Composable
fun JediItemPreview() {
    JediTheme {
        JediItem(jedi = Jedi(1, "Luke Skywalker", "Male", "", "", "", ""))
    }
}

@JediPreview
@Composable
fun JediListPreview() {
    JediTheme {
        JediListContent(
            uiState = jediListPreviewUiState,
        )
    }
}
