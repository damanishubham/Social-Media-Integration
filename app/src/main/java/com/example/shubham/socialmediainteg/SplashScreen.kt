package com.example.shubham.socialmediainteg

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login
import com.facebook.login.widget.LoginButton
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import org.w3c.dom.Text
import com.facebook.Profile.getCurrentProfile
import kotlinx.android.synthetic.main.activity_main.*


class SplashScreen : AppCompatActivity() {

    var fb : LoginButton=  findViewById(R.id.fb_login_button)
    var name: TextView= findViewById(R.id.name)
    var emial: TextView= findViewById(R.id.email)
    var dp: ImageView= findViewById(R.id.profilepic)

    var permissionstring= arrayOf(Manifest.permission.INTERNET)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        fb.setReadPermissions(arrayListOf(
                "public_profile", "email", "user_birthday", "user_friends"));
        var callbackManager = CallbackManager.Factory.create();

        if(!hasallpermission(this@SplashScreen,*permissionstring))
        {
            ActivityCompat.requestPermissions(this@SplashScreen,permissionstring,131)
        }
        else
        {
            val startact = Intent(this@SplashScreen,SplashScreen::class.java)
            startActivity(startact)
            this.finish()
        }

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        startactivity()
                        val profile = Profile.getCurrentProfile()
                        name.setText(profile.getName() + "")
                        emial.setText(profile.id)
                        dp.setImageURI(profile.getProfilePictureUri(200,200))

                    }

                    override fun onCancel() {
                        // App code
                        Log.v("LoginActivity", "cancel");
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                        Log.v("LoginActivity", exception.toString());
                    }
                })

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode)
        {
            131->
            {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    val startact = Intent(this@SplashScreen,MainActivity::class.java)
                    startActivity(startact)
                    this.finish()
                }
                else
                {
                    Toast.makeText(this@SplashScreen,"Please grant permissions to continue",Toast.LENGTH_SHORT).show()
                    this.finish()
                }
            }
            else->
            {
                Toast.makeText(this@SplashScreen,"Something went wrong",Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }
    }
    fun hasallpermission(context: Context, vararg permissions: String): Boolean
    {
        var haspermission= true
        for (permission in permissions)
        {
            var res= context.checkCallingOrSelfPermission(permission)
            if(res != PackageManager.PERMISSION_GRANTED)
            {
                haspermission=false
            }
        }
        return haspermission
    }

    fun startactivity()
    {
        val startact = Intent(this@SplashScreen,MainActivity::class.java)
        startActivity(startact)
        this.finish()
    }
}
