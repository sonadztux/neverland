package com.neverland.capstone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.neverland.capstone.R
import com.neverland.capstone.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    val vbind by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbind.root)

        GlobalScope.launch {
            delay(3000)
            withContext(Dispatchers.Main){
                vbind.splash.root.visibility= View.GONE
                finish()
                startActivity(Intent(this@MainActivity,UploadActivity::class.java))
            }
        }

    }

    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        super.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}