package com.example.pendaftaranapps.ui.addupdatedelete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pendaftaranapps.data.response.AddUpdateResponse
import com.example.pendaftaranapps.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateViewModel : ViewModel() {
    private val _isLodaing = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLodaing

    private val _updateResponse = MutableLiveData<String>()
    val updateResponse: LiveData<String> = _updateResponse

    fun updateSiswa(id: Int, nama: String, alamat: String, jenisKelamin: String, agama: String, sekolahAsal: String){
        _isLodaing.value = true

        val client = ApiConfig.getApiService().updateSiswa(id, nama, alamat, jenisKelamin, agama, sekolahAsal)
        client.enqueue(object : Callback<AddUpdateResponse>{
            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
                _isLodaing.value = false
                _updateResponse.value = "onFailure"
            }

            override fun onResponse(
                call: Call<AddUpdateResponse>,
                response: Response<AddUpdateResponse>
            ) {
                _isLodaing.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _updateResponse.value = responseBody.message
                    } else {
                        _updateResponse.value = "null"
                    }
                }else{
                    _updateResponse.value = "failed"
                }
            }

        })
    }
}