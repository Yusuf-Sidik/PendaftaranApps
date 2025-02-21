package com.example.pendaftaranapps.ui.addupdatedelete

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pendaftaranapps.R
import com.example.pendaftaranapps.data.response.DataItem
import com.example.pendaftaranapps.data.response.DeleteResponse
import com.example.pendaftaranapps.data.retrofit.ApiConfig
import com.example.pendaftaranapps.databinding.ActivityAddUpdateBinding
import com.example.pendaftaranapps.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateBinding

    private var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()).get(AddViewModel::class.java)

        val updateViewModel = ViewModelProvider(this)[UpdateViewModel::class.java]
        val deleteViewModel = ViewModelProvider(this)[DeleteViewModel::class.java]

        val siswa = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(EXTRA_DATA, DataItem::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<DataItem>(EXTRA_DATA)
        }

        if (siswa != null){
            id = siswa.id!!.toInt()

            binding.btnSaveUpdate.text = getString(R.string.update)
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener {
                deleteViewModel.deleteSiswa(id)
                deleteViewModel.deleteResponse.observe(this){deleteResponse ->
                    Toast.makeText(this@AddUpdateActivity, deleteResponse, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                    finish()
                }
                deleteViewModel.isLoading.observe(this){isLoading ->
                    showLoading(isLoading)
                }
//                deleteSiswa(id)
            }

            binding.edtNama.setText("${siswa.nama}")
            binding.edtAlamat.setText("${siswa.alamat}")
            binding.edtJk.setText("${siswa.jenisKelamin}")
            binding.edtAgama.setText("${siswa.agama}")
            binding.edtSekolahAsal.setText("${siswa.sekolahAsal}")
        }else{
            binding.btnSaveUpdate.text = getString(R.string.save)
            binding.btnDelete.visibility = View.GONE
        }

        binding.btnSaveUpdate.setOnClickListener {
            val nama = binding.edtNama.text.toString()
            val alamat = binding.edtAlamat.text.toString()
            val jenisKelamin = binding.edtJk.text.toString()
            val agama = binding.edtAgama.text.toString()
            val sekolahAsal = binding.edtSekolahAsal.text.toString()

            if (nama.isEmpty()){
                binding.edtNama.error = getString(R.string.error)
            }else if (alamat.isEmpty()){
                binding.edtAlamat.error = getString(R.string.error)
            }else if (jenisKelamin.isEmpty()){
                binding.edtJk.error = getString(R.string.error)
            }else if (agama.isEmpty()) {
                binding.edtAgama.error = getString(R.string.error)
            }else if (sekolahAsal.isEmpty()){
                binding.edtSekolahAsal.error = getString(R.string.error)
            }else{
                if(binding.btnSaveUpdate.text == getString(R.string.save)){
                    //ViewModel
                    addViewModel.insertSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
                    addViewModel.addResponse.observe(this){addResponse ->
                        Toast.makeText(this@AddUpdateActivity, addResponse, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                        finish()
                    }
                    //ViewModel
                    addViewModel.isLoading.observe(this){isLoading ->
                        showLoading(isLoading)
                    }
                    //tanpa ViewModel
//                    insertSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
                }else if (binding.btnSaveUpdate.text == getString(R.string.update)){
                    updateViewModel.updateSiswa(id, nama, alamat, jenisKelamin, agama, sekolahAsal)
                    updateViewModel.updateResponse.observe(this){updateResponse ->
                        Toast.makeText(this@AddUpdateActivity, updateResponse, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                        finish()
                    }

                    updateViewModel.isLoading.observe(this){isLoading ->
                        showLoading(isLoading)
                    }
//                    updateSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
                }
            }
        }


    }

//    private fun deleteSiswa(id: Int) {
//        showLoading(true)
//        val client = ApiConfig.getApiService().deleteSiswa(id)
//        client.enqueue(object : Callback<DeleteResponse>{
//            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
//                showLoading(false)
//                Toast.makeText(this@AddUpdateActivity, "onFailure", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResponse(
//                call: Call<DeleteResponse>,
//                response: Response<DeleteResponse>
//            ) {
//                showLoading(false)
//
//                if (response.isSuccessful){
//                    val responseBody = response.body()
//                    if (responseBody != null){
//                        Toast.makeText(this@AddUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
//                        finish()
//                    }else{
//                        Toast.makeText(this@AddUpdateActivity, "null", Toast.LENGTH_SHORT).show()
//                    }
//                }else{
//                    Toast.makeText(this@AddUpdateActivity, "Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        })
//    }


//    private fun updateSiswa(
//        nama: String,
//        alamat: String,
//        jenisKelamin: String,
//        agama: String,
//        sekolahAsal: String
//    ) {
//        showLoading(true)
//
//        val client = ApiConfig.getApiService().updateSiswa(id, nama, alamat, jenisKelamin, agama, sekolahAsal)
//
//        client.enqueue(object : Callback<AddUpdateResponse>{
//            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
//                showLoading(false)
//                Toast.makeText(this@AddUpdateActivity, "onFailure", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResponse(
//                call: Call<AddUpdateResponse>,
//                response: Response<AddUpdateResponse>
//            ) {
//                showLoading(false)
//
//                if (response.isSuccessful){
//                    val responseBody = response.body()
//                    if (responseBody != null){
//                        Toast.makeText(this@AddUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
//                    }else{
//                        Toast.makeText(this@AddUpdateActivity, "null", Toast.LENGTH_SHORT).show()
//                    }
//                }else{
//                    Toast.makeText(this@AddUpdateActivity, "Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        })
//    }

//    tanpa viewModel
//    private fun insertSiswa(
//        nama: String,
//        alamat: String,
//        jenisKelamin: String,
//        agama: String,
//        sekolahAsal: String
//    ) {
//        showLoading(true)
//
//        val client = ApiConfig.getApiService().addSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
//        client.enqueue(object : Callback<AddUpdateResponse>{
//            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
//            }
//
//            override fun onResponse(
//                call: Call<AddUpdateResponse>,
//                response: Response<AddUpdateResponse>
//            ) {
//                showLoading(false)
//
//                if (response.isSuccessful){
//                    val responseBody = response.body()
//
//                    if (responseBody != null){
//                        Toast.makeText(this@AddUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
//                        finish()
//                    }else{
//                        Toast.makeText(this@AddUpdateActivity, "null", Toast.LENGTH_SHORT).show()
//                    }
//                }else{
//                    Toast.makeText(this@AddUpdateActivity, "failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        })
//    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        val EXTRA_DATA = "extra_data"
    }
}