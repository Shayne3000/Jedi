package com.senijoshua.jedi.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Annotation class for the multiple preview configurations of UI components in the app.
 */
@Preview(name = "A: Light Mode", showBackground = true)
@Preview(name = "B: Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class JediPreview
