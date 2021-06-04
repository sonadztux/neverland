package com.neverland.capstone.data.remote


import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("img_output_url")
    val imgOutputUrl: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: String,
    @SerializedName("status")
    val status: Int
)