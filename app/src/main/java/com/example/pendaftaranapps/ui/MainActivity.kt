package com.example.pendaftaranapps.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pendaftaranapps.AddUpdateActivity
import com.example.pendaftaranapps.data.response.DataItem
import com.example.pendaftaranapps.data.response.ListSiswaResponse
import com.example.pendaftaranapps.data.retrofit.ApiConfig
import com.example.pendaftaranapps.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvListSiswa.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListSiswa.addItemDecoration(itemDecoration)

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddUpdateActivity::class.java))
        }

        findAllSiswa()
    }

    private fun findAllSiswa() {
        showLoading(true)

        val client = ApiConfig.getApiService().getAllSiswa()
        client.enqueue(object : Callback<ListSiswaResponse>{
            override fun onFailure(call: Call<ListSiswaResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@MainActivity, "onFailure: ${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ListSiswaResponse>,
                response: Response<ListSiswaResponse>
            ) {
                showLoading(false)

                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        setDataSiswa(responseBody.data)
                    }
                } else {
                    Log.d(TAG, "onFailur: ${response.message()}")
                    Toast.makeText(this@MainActivity, "onFailure: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setDataSiswa(dataSiswa: List<DataItem>) {
        val adapter = ListSiswaAdapter()
        adapter.submitList(dataSiswa)
        binding.rvListSiswa.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}