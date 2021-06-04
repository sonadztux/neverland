package com.neverland.capstone.util

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tapadoo.alerter.Alerter

open class BaseActivity : AppCompatActivity() {

    fun String.showLongToast() {
        Toast.makeText(this@BaseActivity, this, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToast() {
        Toast.makeText(this@BaseActivity, this, Toast.LENGTH_SHORT).show()
    }

    fun showSweetAlert(title: String, desc: String, color: Int) {
        Alerter.create(this)
            .setTitle(title)
            .setText(desc)
            .setBackgroundColorRes(color) // or setBackgroundColorInt(Color.CYAN)
            .show()
    }
}