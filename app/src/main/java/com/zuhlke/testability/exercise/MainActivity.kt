package com.zuhlke.testability.exercise

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuhlke.testability.*
import com.zuhlke.testability.common.TflApiService
import com.zuhlke.testability.common.TubeLine
import com.zuhlke.testability.common.TubeLineStatus
import com.zuhlke.testability.common.TubeStatusCache
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

        tube_status_rv.layoutManager = LinearLayoutManager(this)

        val tflApiService = Retrofit.Builder()
            .baseUrl("https://api.tfl.gov.uk")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TflApiService::class.java)

        if (TubeStatusCache.tubeLines.isNotEmpty()) {
            Log.i(MainActivity::class.java.simpleName, "retrieving status from cache")
            tube_status_rv.adapter = TubeStatusAdapter(TubeStatusCache.tubeLines)
        } else {
            Log.i(MainActivity::class.java.simpleName, "retrieving status from network")
            tflApiService.tubeStatus().enqueue(object: Callback<List<Map<String, Any>>> {
                override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "network call failure", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                    // TODO Can we handle optional unwrapping better?
                    val tubeStatus = response.body()!!.map { line ->
                        val id = line["id"] as String
                        val name = line["name"] as String
                        val status = (line["lineStatuses"] as List<*>).map { lineStatus ->

                            val lineStatusItem = lineStatus as Map<*,*>
                            TubeLineStatus(
                                (lineStatusItem["statusSeverity"] as Double).toInt(),
                                lineStatusItem["statusSeverityDescription"] as String
                            )
                        }

                        TubeLine(id, name, status)
                    }
                    TubeStatusCache.tubeLines = tubeStatus
                    tube_status_rv.adapter = TubeStatusAdapter(tubeStatus)
                }
            })
        }
    }
}