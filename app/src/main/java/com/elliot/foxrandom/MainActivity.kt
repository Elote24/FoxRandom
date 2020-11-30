package com.elliot.foxrandom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var swiperefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageViewFox)
        textView = findViewById(R.id.textViewURL)

        swiperefresh = findViewById(R.id.refreshLayout)

        swiperefresh.setOnRefreshListener {
            this.refreshData()
        }

        this.refreshData()

    }

    private fun refreshData(){
        RetrofitCreate.foxservice.getRandomFox().enqueue(object : Callback<Fox> {
            override fun onResponse(call: Call<Fox>, response: Response<Fox>) {
                if(response.isSuccessful){
                    val fox = response.body()
                    fox?.apply {
                        Glide.with(this@MainActivity)
                            .load(this.image)
                            .into(imageView)

                        textView.text = this.link
                    }

                    swiperefresh.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<Fox>, t: Throwable) {
                Log.e(TAG,t.message,t)
                Toast.makeText(this@MainActivity,"Ocurrio un error", Toast.LENGTH_SHORT).show()
            }

        })
    }
}