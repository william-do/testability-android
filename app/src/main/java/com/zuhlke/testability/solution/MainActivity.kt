package com.zuhlke.testability.solution

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuhlke.testability.*
import com.zuhlke.testability.common.TflApiService
import com.zuhlke.testability.common.TubeLine
import com.zuhlke.testability.common.TubeStatusCache
import com.zuhlke.testability.exercise.TubeStatusAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tube_status_rv.layoutManager = LinearLayoutManager(this@MainActivity)

        val tflApiService = Retrofit.Builder()
            .baseUrl("https://api.tfl.gov.uk")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TflApiService::class.java)

        val viewModel = MainViewModel(tflApiService, TubeStatusCache)

        viewModel.downloadStatusFromTfl(
            onError = {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                tube_status_rv.adapter = TubeStatusAdapter(it)
            }
        )
    }
}

class MainViewModel(private val tflApiService: TflApiService, private val statusCache: TubeStatusCache) {

    fun downloadStatusFromTfl(onError: (String) -> Unit, onSuccess: (List<TubeLine>) -> Unit) {

        if (statusCache.tubeLines.isNotEmpty()) {
            onSuccess(statusCache.tubeLines)
        } else {
            tflApiService.tubeStatus().enqueue(object : Callback<List<Map<String, Any>>> {
                override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                    onError(t.message ?: "network call failed with no message")
                }

                override fun onResponse(
                    call: Call<List<Map<String, Any>>>,
                    response: Response<List<Map<String, Any>>>
                ) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess(toTubeStatus(responseBody))
                    } else {
                        onError("response was null")
                    }

                }
            })
        }

    }

}
