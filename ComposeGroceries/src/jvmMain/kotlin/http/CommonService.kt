package http

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface CommonService {
    @GET("/alive")
    suspend fun isAlive():Response<ResponseBody>
}