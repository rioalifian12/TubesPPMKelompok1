package id.ac.unpas.tubes.networks

import com.skydoves.sandwich.ApiResponse
import id.ac.unpas.tubes.model.SepedaMotor
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SepedaMotorApi {
    @GET("api/sepeda-motor")
    suspend fun all(): ApiResponse<SepedaMotorGetResponse>
    @GET("api/sepeda-motor/{id}")
    suspend fun find(@Path("id") id: String):
            ApiResponse<SepedaMotorSingleGetResponse>
    @POST("api/sepeda-motor")
    @Headers("Content-Type: application/json")
    suspend fun insert(@Body item: SepedaMotor):
            ApiResponse<SepedaMotorSingleGetResponse>
    @PUT("api/sepeda-motor/{id}")
    @Headers("Content-Type: application/json")
    suspend fun update(@Path("id") pathId: String,
                       @Body item: SepedaMotor
    ): ApiResponse<SepedaMotorSingleGetResponse>
    @DELETE("api/sepeda-motor/{id}")
    suspend fun delete(@Path("id") id: String):
            ApiResponse<SepedaMotorSingleGetResponse>
}