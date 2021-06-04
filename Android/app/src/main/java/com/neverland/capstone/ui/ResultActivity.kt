package com.neverland.capstone.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import com.bumptech.glide.Glide
import com.neverland.capstone.R
import com.neverland.capstone.data.remote.UploadResponse
import com.neverland.capstone.databinding.ActivityResultBinding
import com.neverland.capstone.util.BaseActivity
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ResultActivity : BaseActivity() {

    companion object {
        const val RESULT_OBJ = "rizalt_objekt"
    }


    val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val objectResult = intent.extras?.getParcelable<UploadResponse>(RESULT_OBJ)


        if (objectResult?.result?.contains("REAL", ignoreCase = true) == true) {
            binding.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(12))
                .setPosition(-50f, binding.viewKonfetti.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
            showSweetAlert(
                "Real",
                "Gambarmu Terdeksi Real dengan probabilitas ${objectResult?.result}",
                R.color.alerter_default_success_background
            )
        } else {
            showSweetAlert(
                "Fake",
                "Gambarmu Terdeksi Palsu atau Telah Diedit dengan probabilitas ${objectResult?.result}",
                R.color.alert_default_error_background
            )
        }

        binding.resultText.text = objectResult?.result

        binding.root.let {
            Glide
                .with(it)
                .load(objectResult?.imgOutputUrl)
                .skipMemoryCache(true)
                .dontAnimate()
                .thumbnail(Glide.with(it).load(R.raw.loading2))
                .placeholder(R.drawable.ic_loading_small_1)
                .into(binding.resultImage)
        }

        binding.btnShare.setOnClickListener {

        }

    }

    fun shareImage(){
        val shareIntent: Intent
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        var path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .toString() + "/Share.png"
        var out: OutputStream? = null
        val file = File(path)
        try {
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        path = file.path
        val bmpUri = Uri.parse("file://$path")
        shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey please check this application https://play.google.com/store/apps/details?id=$packageName"
        )
        shareIntent.type = "image/png"
        startActivity(Intent.createChooser(shareIntent, "Share with"))
    }


    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        super.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}