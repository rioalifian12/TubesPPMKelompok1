package id.ac.unpas.tubes.networks

import com.skydoves.sandwich.ApiResponse
import id.ac.unpas.tubes.model.Mobil
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MobilApi {
    @GET("api/mobil")
    suspend fun all(): ApiResponse<MobilGetResponse>
    @GET("api/mobil/{id}")
    suspend fun find(@Path("id") id: String):
            ApiResponse<MobilSingleGetResponse>
    @POST("api/mobil")
    @Headers("Content-Type: application/json")
    suspend fun insert(@Body item: Mobil):
            ApiResponse<MobilSingleGetResponse>
    @PUT("api/mobil/{id}")
    @Headers("Content-Type: application/json")
    suspend fun update(@Path("id") pathId: String,
                       @Body item: Mobil
    ): ApiResponse<MobilSingleGetResponse>
    @DELETE("api/mobil/{id}")
    suspend fun delete(@Path("id") id: String):
            ApiResponse<MobilSingleGetResponse>
}