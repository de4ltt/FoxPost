package com.transport.ui.util

import android.content.Context
import android.widget.Toast
import com.transport.ui.theme.Strings

object Validator {
    val onlyDigits: (Context, String, () -> Unit) -> Unit = { context, text, ifTrueAction ->
        ToastedTriedRequire(
            context = context,
            condition = Regex("[0-9]*").matches(text),
            errorMessage = "Можно ввести только числа",
            ifTrueAction = ifTrueAction
        )
    }

    private fun ToastedTriedRequire(
        context: Context,
        condition: Boolean,
        errorMessage: String,
        ifTrueAction: () -> Unit
    ) {
        try {
            require(condition) {
                errorMessage
            }
            ifTrueAction()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                e.message ?: Strings.UNKNOWN_ERROR,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}