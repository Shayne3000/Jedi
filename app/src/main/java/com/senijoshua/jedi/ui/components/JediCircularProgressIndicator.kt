package com.senijoshua.jedi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.senijoshua.jedi.R
import com.senijoshua.jedi.ui.theme.JediTheme
import com.senijoshua.jedi.ui.util.JediPreview

@Composable
fun JediCircularProgressIndicator(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(dimensionResource(id = R.dimen.progress_indicator_width)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@JediPreview
@Composable
private fun JediCircularProgressIndicatorPreview() {
    JediTheme {
        JediCircularProgressIndicator()
    }
}
