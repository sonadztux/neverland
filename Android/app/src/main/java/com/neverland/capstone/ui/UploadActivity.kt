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
import com.neverland.capstone.R
import com.neverland.capstone.databinding.ActivityUploadBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class UploadActivity : AppCompatActivity() {

    companion object {
        private const val MY_CAMERA_REQUEST_CODE = 100
        private const val CODE_RESULT_URI = "res_cam_uro"
    }

    var fileUpload = "";

    val vbind by lazy { ActivityUploadBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbind.root)


        initPhoto()

        vbind.btnScan.visibility= View.GONE
        vbind.btnPickFile.setOnClickListener {
            takePicture()
        }

        vbind.ivImg.clipToOutline=true

        vbind.btnScan.setOnClickListener {
            vbind.includeLoading.root.visibility=View.VISIBLE
        }

        vbind.includeLoading.btnCancel.setOnClickListener {
            vbind.includeLoading.root.visibility=View.GONE
        }
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
        Toast.makeText(this,"Take Picture",Toast.LENGTH_LONG).show()
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
                vbind.btnScan.visibility=View.VISIBLE

                vbind.btnPickFile.text = "Changes"
                vbind.ivImg.setImageURI(resultUri.path?.toUri())
                fileUpload = resultUri.toString();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Camera Error", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Result Code Unknown", Toast.LENGTH_SHORT).show()
        }
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