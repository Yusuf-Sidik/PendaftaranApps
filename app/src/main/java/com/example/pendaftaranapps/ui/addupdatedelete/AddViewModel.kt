package com.example.pendaftaranapps.ui.addupdatedelete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pendaftaranapps.data.response.AddUpdateResponse
import com.example.pendaftaranapps.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _addResponse = MutableLiveData<String>()
    val addResponse: LiveData<String> = _addResponse

    fun insertSiswa(nama: String, alamat: String, jenisKelamin: String, agama: String, sekolahAsal: String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().addSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
        client.enqueue(object : Callback<AddUpdateResponse>{
            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
                _isLoading.value = false
                _addResponse.value = "onFailure"
            }

            override fun onResponse(
                call: Call<AddUpdateResponse>,
                response: Response<AddUpdateResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful){
                    val responseBody = response.body()

                    if (responseBody != null){
                        _addResponse.value = responseBody.message
                    } else {
                        _addResponse.value = "null"
                    }
                } else {
                    _addResponse.value = "failde"
                }
            }

        })
    }
}