package com.neverland.capstone.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.bumptech.glide.Glide
import com.neverland.capstone.R
import com.neverland.capstone.data.remote.UploadResponse
import com.neverland.capstone.databinding.ActivityResultBinding
import com.neverland.capstone.util.BaseActivity
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size


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
                "your image is detected as genuine with authenticity level of ${objectResult.result}",
                R.color.alerter_default_success_background
            )
        } else {
            showSweetAlert(
                "Fake",
                "your image is detected as genuine with authenticity level of ${objectResult?.result}",
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

        binding.btnTryOther.setOnClickListener {
            finish()
            super.onBackPressed()
        }



        binding.btnShare.setOnClickListener {
            objectResult?.let { it1 -> share(it1) }
        }
    }

    private fun share(model: UploadResponse) {
        val intent = Intent(Intent.ACTION_SEND)
        /*This will be the actual content you wish you share.*/
        val shareBody = "Hi guys, i just use Photocuration app \n" +
                "to detect the level of authenticity of faces in images with an authenticity level with result ${model.result},\n" +
                "link : ${model.imgOutputUrl}.\n\n" +
                "Download Photocuration to try your own !"
        intent.type = "text/plain"
        /*Applying information Subject and Body.*/
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            "test"
        )
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        /*Fire!*/
        /*Fire!*/startActivity(Intent.createChooser(intent, "Share"))
    }


    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        super.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}