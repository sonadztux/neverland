package com.neverland.capstone.data

import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.neverland.capstone.data.network.CapstoneEndpoint
import com.neverland.capstone.data.network.Resource
import com.neverland.capstone.data.remote.UploadResponse
import com.neverland.capstone.util.URL
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File


class CapstoneRepository(private val api: CapstoneEndpoint) {


    fun uploadImage(image: File)  : MutableLiveData<Resource<UploadResponse>>  {
        val sendResponse: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()
        sendResponse.value = Resource.Loading()
        AndroidNetworking.upload(URL.UPLOAD)
            .addMultipartFile("file",image)
            .build()
            .getAsString(object : StringRequestListener{
                override fun onResponse(response: String?) {
                    Timber.d("uploadz onresponse : ${response.toString()}")
                    val gson = Gson()
                    val objectRes = gson.fromJson<UploadResponse>(response,UploadResponse::class.java)
                    sendResponse.value = Resource.Success(objectRes)

                }

                override fun onError(anError: ANError?) {
                    sendResponse.value = Resource.Error("An Error Occured")
                    Timber.d("uploadz onError : ${anError.toString()}")
                    Timber.d("uploadz onError : ${anError?.errorBody.toString()}")
                }
            })

        val photoBody = image.asRequestBody()
        val photoPart = MultipartBody.Part.createFormData(
            "photo",
            image.name, photoBody
        )

        return sendResponse
    }


}