package com.mediamonks.testplacesautocomplete.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.mediamonks.testplacesautocomplete.util.TextChangeWatcher
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Created on 06/12/2018.
 */
class DebouncedEditText : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val textChangeEmitter: PublishSubject<String> = PublishSubject.create()

    init {
        addTextChangedListener(object : TextChangeWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textChangeEmitter.onNext(s.toString())
            }
        })
    }

    fun getInput(): Observable<String> = textChangeEmitter.debounce(500, TimeUnit.MILLISECONDS)
}