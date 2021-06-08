package com.neverland.capstone.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidnetworking.AndroidNetworking
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.neverland.capstone.R
import com.neverland.capstone.data.network.Resource
import com.neverland.capstone.databinding.ActivityUploadBinding
import com.neverland.capstone.ui.ResultActivity.Companion.RESULT_OBJ
import com.neverland.capstone.util.BaseActivity
import com.neverland.capstone.viewmodel.AnalyzeViewModel
import com.neverland.capstone.viewmodel.ViewModelFactory
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.io.File
import java.io.IOException

class UploadActivity : BaseActivity(), KodeinAware {

    override val kodein by kodein()
    private val viewModelFactory: ViewModelFactory by instance()

    lateinit var viewModel: AnalyzeViewModel

    companion object {
        private const val MY_CAMERA_REQUEST_CODE = 100
        private const val CODE_RESULT_URI = "res_cam_uro"
    }

    var fileUpload = "";

    val vbind by lazy { ActivityUploadBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbind.root)

        setupViewModel()
        initPhoto()

        // High-accuracy landmark detection and face classification
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        // Real-time contour detection
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()


        vbind.btnScan.visibility = View.GONE
        vbind.btnPickFile.setOnClickListener {
            takePicture()
        }

        vbind.includeLoading.btnCancel.setOnClickListener {
            vbind.includeLoading.root.visibility = View.GONE
            AndroidNetworking.cancelAll(); // All the requests will be cancelled.
            AndroidNetworking.forceCancelAll(); // All the requests will be cancelled , even if any percent threshold is set ,
        }

        vbind.ivImg.clipToOutline = true

        vbind.includeLoading.btnCancel.setOnClickListener {
            vbind.includeLoading.root.visibility = View.GONE
        }

        vbind.btnScan.setOnClickListener {
            FileUtils.getFile(this, fileUpload.toUri())?.let { it1 ->
                viewModel.uploadImage(it1).observe(this, Observer {
                    when (it) {
                        is Resource.Success -> {
                            Timber.d("on upload success")
                            vbind.includeLoading.root.visibility = View.GONE
                            "Berhasil Mengupload Foto".showLongToast()
                            val sendedObject = it.data
                            startActivity(
                                Intent(this, ResultActivity::class.java)
                                    .putExtra(RESULT_OBJ, it.data)
                            )
                        }
                        is Resource.Loading -> {
                            Timber.d("on upload loading")
                            vbind.includeLoading.root.visibility = View.VISIBLE
                        }
                        is Resource.Error -> {
                            Timber.d("on upload error")
                            "Gagal Mengupload Foto".showLongToast()
                            vbind.includeLoading.root.visibility = View.GONE
                        }
                        else -> {
                        }
                    }
                })
            }
        }

    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(AnalyzeViewModel::class.java)
    }

    private fun initPhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                MY_CAMERA_REQUEST_CODE
            )
        }
    }

    private fun takePicture() {
        resetBorderedBg()
        Toast.makeText(this, "Take Picture", Toast.LENGTH_LONG).show()
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                val image: InputImage
                try {
                    image = InputImage.fromFilePath(this, resultUri)
                    val highAccuracyOpts = FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build()

                    vbind.ivImg.setImageURI(resultUri.path?.toUri())


                    val detector = FaceDetection.getClient(highAccuracyOpts)
                    detector.process(image)
                        .addOnSuccessListener { faces ->
                            when {
                                faces.size > 1 -> {
                                    resetBorderedBg()
                                    "${faces.size} Wajah Terdeteksi , Anda hanya dapat mendeteksi 1 wajah".showLongToast()
                                }
                                faces.size == 0 -> {
                                    resetBorderedBg()
                                    "Tidak ada Wajah Terdeteksi".showLongToast()
                                }
                                else -> {
                                    vbind.imgContainer.background = null
                                    vbind.btnScan.visibility = View.VISIBLE
                                    vbind.btnPickFile.text = "Changes"
                                    fileUpload = resultUri.toString();
                                    "${faces.size} Wajah Terdeteksi".showLongToast()
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Timber.e("error${e}")
                            resetBorderedBg()
                            "Wajah Tidak Terdeteksi, Silakan Pilih Wajah Lain".showLongToast()
                        }


                } catch (e: IOException) {
                    vbind.imgContainer.background =
                        (ContextCompat.getDrawable(this, R.drawable.rounded_bordered))
                    e.printStackTrace()
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                vbind.imgContainer.background =
                    (ContextCompat.getDrawable(this, R.drawable.rounded_bordered))
                Toast.makeText(this, "Camera Error", Toast.LENGTH_SHORT).show()
            }
        } else {
            vbind.imgContainer.background =
                (ContextCompat.getDrawable(this, R.drawable.rounded_bordered))
            Toast.makeText(this, "Result Code Unknown", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetBorderedBg() {
        vbind.btnScan.visibility = View.GONE
        vbind.btnPickFile.text = "Select File"
        vbind.imgContainer.background =
            (ContextCompat.getDrawable(this, R.drawable.rounded_bordered))
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        super.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

}