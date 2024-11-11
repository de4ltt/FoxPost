package com.transport.ui.util

import android.content.Context
import android.widget.Toast
import com.transport.ui.theme.Strings

object Validator {
    val onlyDigitsLessThan1000: (Context, String) -> Boolean
        get() = { context, text ->
            context.ToastedTriedRequire(
                condition = Regex("[0-9]*").matches(text),
                errorMessage = "Можно ввести только числа"
            )
            context.ToastedTriedRequire(
                condition = text.ifEmpty { "0" }.toInt() < 1000,
                errorMessage = "Числа не должны быть больше 1000"
            )
        }

    private fun Context.ToastedTriedRequire(
        condition: Boolean,
        errorMessage: String
    ): Boolean {
        try {
            require(condition) {
                errorMessage
            }
            return true
        } catch (e: Exception) {
            Toast.makeText(
                this,
                e.message ?: Strings.UNKNOWN_ERROR,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }
}