package com.quietlog.app.debug

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * No-op stub. The real panel (data seeding for local testing) lives in `src/debug` and replaces
 * this file for debug builds via Gradle's variant source sets — release builds compile this
 * empty version instead, so the debug tooling is absent from the release APK entirely, not just
 * hidden behind a runtime check.
 */
@Composable
fun DebugToolsSection(modifier: Modifier = Modifier) {
}
