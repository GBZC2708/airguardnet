package com.airguardnet.mobile.data.remote

import com.airguardnet.mobile.core.network.ApiResponse
import com.airguardnet.mobile.data.remote.dto.AlertDto
import com.airguardnet.mobile.data.remote.dto.LoginRequestDto
import com.airguardnet.mobile.data.remote.dto.LoginResponseDto
import com.airguardnet.mobile.data.remote.dto.ConfigParameterDto
import com.airguardnet.mobile.data.remote.dto.DeviceDto
import com.airguardnet.mobile.data.remote.dto.ReadingDto
import com.airguardnet.mobile.data.remote.dto.SensorConfigDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AirGuardNetApiService {
    @POST("login")
    suspend fun login(@Body body: LoginRequestDto): ApiResponse<LoginResponseDto>

    @GET("devices")
    suspend fun getDevices(): ApiResponse<List<DeviceDto>>

    @GET("devices/{id}")
    suspend fun getDevice(@Path("id") id: Long): ApiResponse<DeviceDto>

    @GET("devices/{id}/readings")
    suspend fun getDeviceReadings(@Path("id") id: Long): ApiResponse<List<ReadingDto>>

    @GET("devices/{id}/alerts")
    suspend fun getDeviceAlerts(@Path("id") id: Long): ApiResponse<List<AlertDto>>

    @GET("alerts")
    suspend fun getAlerts(): ApiResponse<List<AlertDto>>

    @GET("config-parameters")
    suspend fun getConfigParameters(): ApiResponse<List<ConfigParameterDto>>

    @GET("sensor-configs")
    suspend fun getSensorConfigs(): ApiResponse<List<SensorConfigDto>>
}
