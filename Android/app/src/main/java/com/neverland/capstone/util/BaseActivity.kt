package com.neverland.capstone.util

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    fun String.showLongToast() {
        Toast.makeText(this@BaseActivity, this, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToast() {
        Toast.makeText(this@BaseActivity, this, Toast.LENGTH_SHORT).show()
    }
}