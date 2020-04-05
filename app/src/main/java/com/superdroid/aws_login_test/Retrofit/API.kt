package com.superdroid.aws_login_test.Retrofit

import android.telecom.Call
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface API {

    @FormUrlEncoded
    @POST ("idCheck.php") //아이디 중복 검사
    fun get_IdCheck(@Field("Id") Id: String): retrofit2.Call<ResponseBody>

    @POST ("signUp.php") // 회원가입
    @FormUrlEncoded
    fun signUp(@Field("Id") Id : String, @Field("Password") Password : String,
               @Field("Nickname") Nickname : String, @Field("Age") Age : String, @Field("Gender") Gender : String) : retrofit2.Call<ResponseBody>

    @POST("logIn.php") // 로그인
    @FormUrlEncoded
    fun login(@Field("Id") Id : String, @Field("Password") Password: String) : retrofit2.Call<ResponseBody>
}