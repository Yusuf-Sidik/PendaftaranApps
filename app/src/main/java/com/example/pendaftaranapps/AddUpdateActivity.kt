package com.example.pendaftaranapps

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pendaftaranapps.data.response.AddUpdateResponse
import com.example.pendaftaranapps.data.response.DataItem
import com.example.pendaftaranapps.data.retrofit.ApiConfig
import com.example.pendaftaranapps.databinding.ActivityAddUpdateBinding
import com.example.pendaftaranapps.ui.MainActivity
import com.google.android.material.textfield.TextInputEditText
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
                deleteSiswa(id)
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
                    insertSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
                }else if (binding.btnSaveUpdate.text == getString(R.string.update)){
                    updateSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
                }
            }
        }


    }

    private fun deleteSiswa(id: Int) {
        showLoading(true)
        val client = ApiConfig.getApiService().deleteSiswa(id)
        client.enqueue(object : Callback<AddUpdateResponse>{
            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@AddUpdateActivity, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<AddUpdateResponse>,
                response: Response<AddUpdateResponse>
            ) {
                showLoading(false)

                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        Toast.makeText(this@AddUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@AddUpdateActivity, "null", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@AddUpdateActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


    private fun updateSiswa(
        nama: String,
        alamat: String,
        jenisKelamin: String,
        agama: String,
        sekolahAsal: String
    ) {
        showLoading(true)

        val client = ApiConfig.getApiService().updateSiswa(id, nama, alamat, jenisKelamin, agama, sekolahAsal)

        client.enqueue(object : Callback<AddUpdateResponse>{
            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@AddUpdateActivity, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<AddUpdateResponse>,
                response: Response<AddUpdateResponse>
            ) {
                showLoading(false)

                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        Toast.makeText(this@AddUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                    }else{
                        Toast.makeText(this@AddUpdateActivity, "null", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@AddUpdateActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun insertSiswa(
        nama: String,
        alamat: String,
        jenisKelamin: String,
        agama: String,
        sekolahAsal: String
    ) {
        showLoading(true)

        val client = ApiConfig.getApiService().addSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
        client.enqueue(object : Callback<AddUpdateResponse>{
            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<AddUpdateResponse>,
                response: Response<AddUpdateResponse>
            ) {
                showLoading(false)

                if (response.isSuccessful){
                    val responseBody = response.body()

                    if (responseBody != null){
                        Toast.makeText(this@AddUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@AddUpdateActivity, "null", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@AddUpdateActivity, "failed", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

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