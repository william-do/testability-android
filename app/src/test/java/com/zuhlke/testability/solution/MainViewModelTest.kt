package com.zuhlke.testability.solution

import com.zuhlke.testability.common.TflApiService
import com.zuhlke.testability.common.TubeStatusCache
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.time.Duration

class MainViewModelTest {
    @Test
    fun `download status is successful`() {
        val url = this::class.java.classLoader!!.getResource("status-response.json")
        val response = File(url.toURI()).readBytes()

        val server = MockWebServer()
        server.enqueue(MockResponse().setBody(String(response)))
        server.start()

        val tflApiService = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TflApiService::class.java)

        val viewModel = MainViewModel(tflApiService, TubeStatusCache)

        var successCalled = false
        var errorCalled = false

        viewModel.downloadStatusFromTfl(
            onSuccess = { successCalled = true },
            onError = { errorCalled = true }
        )

        await().atMost(Duration.ofSeconds(5)).until { successCalled }
        assertTrue(successCalled)
        assertFalse(errorCalled)
    }

    @Test
    fun `download status has error`() {
        val server = MockWebServer()
        server.enqueue(MockResponse().setResponseCode(500))
        server.start()

        val tflApiService = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TflApiService::class.java)

        val viewModel = MainViewModel(tflApiService, TubeStatusCache)

        var successCalled = false
        var errorCalled = false

        viewModel.downloadStatusFromTfl(
            onSuccess = { successCalled = true },
            onError = { errorCalled = true }
        )

        await().atMost(Duration.ofSeconds(5)).until { errorCalled }
        assertTrue(errorCalled)
        assertFalse(successCalled)
    }
}