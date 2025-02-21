package com.example.pendaftaranapps.ui.addupdatedelete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pendaftaranapps.data.response.DeleteResponse
import com.example.pendaftaranapps.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _deleteResponse = MutableLiveData<String>()
    val deleteResponse: LiveData<String> = _deleteResponse

    fun deleteSiswa(id: Int){
        _isLoading.value = true

        val client = ApiConfig.getApiService().deleteSiswa(id)
        client.enqueue(object : Callback<DeleteResponse>{
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                _isLoading.value = false
                _deleteResponse.value = t.message
            }

            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful){
                    _deleteResponse.value = response.body()?.message
                } else {
                    _deleteResponse.value = "failed"
                }
            }

        })
    }
}