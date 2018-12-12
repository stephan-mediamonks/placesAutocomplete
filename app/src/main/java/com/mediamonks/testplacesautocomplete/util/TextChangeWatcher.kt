package com.mediamonks.testplacesautocomplete.util

import android.text.Editable
import android.text.TextWatcher

/**
 * Created on 06/12/2018.
 */
abstract class TextChangeWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
}