package com.zuhlke.testability.common

import retrofit2.Call
import retrofit2.http.GET

interface TflApiService {

    @GET("Line/Mode/tube/Status")
    fun tubeStatus(): Call<List<Map<String, Any>>>

}

data class TubeLine(val id: String, val name: String, val statuses: List<TubeLineStatus>)

data class TubeLineStatus(val severity: Int, val description: String)

object TubeStatusCache {

    var tubeLines: List<TubeLine> = emptyList()

}