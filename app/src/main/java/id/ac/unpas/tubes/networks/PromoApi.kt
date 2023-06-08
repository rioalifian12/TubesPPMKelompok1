package id.ac.unpas.tubes.networks

import com.skydoves.sandwich.ApiResponse
import id.ac.unpas.tubes.model.Promo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PromoApi {
    @GET("api/promo")
    suspend fun all(): ApiResponse<PromoGetResponse>
    @GET("api/promo/{id}")
    suspend fun find(@Path("id") id: String):
            ApiResponse<PromoSingleGetResponse>
    @POST("api/promo")
    @Headers("Content-Type: application/json")
    suspend fun insert(@Body item: Promo):
            ApiResponse<PromoSingleGetResponse>
    @PUT("api/promo/{id}")
    @Headers("Content-Type: application/json")
    suspend fun update(@Path("id") pathId: String,
                       @Body item: Promo
    ): ApiResponse<PromoSingleGetResponse>
    @DELETE("api/promo/{id}")
    suspend fun delete(@Path("id") id: String):
            ApiResponse<PromoSingleGetResponse>
}