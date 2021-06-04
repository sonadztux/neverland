package com.neverland.capstone.data.remote


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadResponse(
    @SerializedName("img_output_url")
    val imgOutputUrl: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: String,
    @SerializedName("status")
    val status: Int
) : Parcelable