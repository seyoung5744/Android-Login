package com.superdroid.aws_login_test.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest.permission.INTERNET
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.EditText
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import com.superdroid.aws_login_test.join.SignUpActivity
import com.superdroid.aws_login_test.LoginSueccessActivity
import com.superdroid.aws_login_test.R
import com.superdroid.aws_login_test.Retrofit.RetrofitClient
import okhttp3.ResponseBody

import retrofit2.Call;
import retrofit2.Response;
import com.superdroid.aws_login_test.common.Utils
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    var WSY = "WSY"

    private var mEmailView: EditText? = null
    private var mPasswordView: EditText? = null

    private var email : String? = null
    private var password : String? = null
    private var loginChecked : Boolean? = null
    private var settings : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkSelfPermission(INTERNET) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this, arrayOf( INTERNET ),
                1
            )
        }

        Log.d(WSY,Utils.getAutoLogin(this).toString())
        mEmailView = findViewById(R.id.login_id) as EditText
        mPasswordView = findViewById(R.id.login_password) as EditText
    } // onCreate

    fun onClick(v : View?){
        when(v!!.id){
            R.id.button_login -> {
                email = mEmailView?.getText().toString()
                password = mPasswordView?.getText().toString()
                Log.d(WSY,email + ", " + password)
                RetrofitClient.retrofitService.login(email!!,password!!).enqueue(object : retrofit2.Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        try {
                            val result = response.body() as ResponseBody
                            val resultValue = result.string()

                            Log.i(WSY,"결과 : " + resultValue)

                            var userData = JSONObject(resultValue)

                            Log.i(WSY,userData.getString("Id") + ", " + userData.getString("Nickname"))
                            Utils.setEMAILData(this@MainActivity,userData.getString("Id"))
                            Utils.setPWDData(this@MainActivity,userData.getString("Password"))
                            Utils.setIDData(this@MainActivity,userData.getString("Nickname"))
                            Utils.setAutoLogin(this@MainActivity,true)

                            var nextIntent = Intent(this@MainActivity, LoginSueccessActivity::class.java)
                            startActivity(nextIntent)
                            finish()
                        }catch (e : Exception){
                            e.toString()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }
                })
            }
            R.id.button_signup -> {
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                startActivityForResult(intent,101)
            }
//            R.id.button_google_login -> {
//
//            }
        }
    }

    // 회원가입 성공 시 자동 email, password입력
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101 && resultCode == RESULT_OK){
            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            mEmailView?.setText(data?.getStringExtra("ID"))
            mPasswordView?.setText(data?.getStringExtra("PW"))
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

  /*  private fun showProgress(show: Boolean) {
        mProgressView.setVisibility(if (show) View.VISIBLE else View.GONE)
    }*/
}
