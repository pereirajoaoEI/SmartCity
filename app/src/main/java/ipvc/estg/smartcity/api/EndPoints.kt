package ipvc.estg.smartcity.api


import retrofit2.http.*


interface EndPoints {
    @FormUrlEncoded
    @POST("userverification")
    fun verifyUsers(@Field("username")username:String, @Field("password")password:String): retrofit2.Call<User>

//    @GET("/user/{id}")
//    fun getUserById(@Path("id") id: Int): Call<User>

//    @FormUrlEncoded
//    @POST("/posts")
//    fun postTest(@Field("title") first: String?): retrofit2.Call<OutputPost>

}