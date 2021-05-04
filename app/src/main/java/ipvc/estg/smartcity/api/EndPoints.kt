package ipvc.estg.smartcity.api


import retrofit2.http.*


interface EndPoints {
    @FormUrlEncoded
    @POST("userverification")
    fun verifyUsers(@Field("username")username:String, @Field("password")password:String): retrofit2.Call<User>

    @GET("users/anomalias")
    fun getAllAnomalias(): retrofit2.Call<List<Markers>>


    //filtro
    @GET("myuser/{id}/anomalias")
    fun getAllUserAnomalias(@Path("id")id_username:Int): retrofit2.Call<List<Markers>>

//    @FormUrlEncoded
//    @POST("insertAnomalia")
//    fun insertAnomalias(@Field("lat")latitude:Double, @Field("longi")longitude:Double, @Field("foto")imagem:String,@Field("login_id")userId:Int): retrofit2.Call<Markers>

    @FormUrlEncoded
    @POST("insertAnomalia")
    fun criarAnomalias(@Field("titulo") titulo: String?, @Field("descricao") descricao: String?,
                        @Field("lat") lat: String?, @Field("longi") longi: String?,
                        @Field("foto") foto: String?, @Field("login_id") login_id: Int?,
                       @Field("tipo") tipo: String?
    ) : retrofit2.Call<Markers>

//    @FormUrlEncoded
//    @POST("/posts")
//    fun postTest(@Field("title") first: String?): retrofit2.Call<OutputPost>

    @GET("anomalias/{tipo}")
    fun getAllAnomaliasTipo(@Path("tipo")tipo:String): retrofit2.Call<List<Markers>>

}