package com.neverland.capstone.data.network

import com.neverland.capstone.data.remote.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File


interface CapstoneEndpoint {

//
//    @Multipart
//    @POST("predict_image")
//    fun uploadFilze(
//        @Part("file") file: File?,
//    ): Call<UploadResponse>

    @Multipart
    @POST("predict_image")
    fun uploadFile(@Part body: MultipartBody.Part?): Call<ResponseBody>


}