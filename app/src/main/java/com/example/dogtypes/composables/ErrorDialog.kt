package com.example.dogtypes.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.example.dogtypes.R


@Composable
fun ErrorDialog(
    status: Int,
    onErrorDialogDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.semantics { testTag = "error-dialog" },
        onDismissRequest = { },
        title = {
            Text(stringResource(R.string.error_dialog_title))
        },
        text = {
            Text(stringResource(id = status))
        },
        confirmButton = {
            Button(onClick = { onErrorDialogDismiss() }) {
                Text(stringResource(R.string.try_again))
            }
        }
    )
}