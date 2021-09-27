package kobot.board.gasos.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kobot.board.gasos.R
import kobot.board.gasos.fragment.HomeFragment
import kobot.board.gasos.fragment.NotificationFragment
import kobot.board.gasos.fragment.ProfileFragment
import kobot.board.gasos.fragment.SearchFragment
import kobot.board.gasos.util.GasosMessagingService
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.jar.Manifest

class MainActivity : AnimationActivity(TransitionMode.HORIZON_REVERSE) {

    private val homeFragment by lazy { HomeFragment() }
    private val searchFragment by lazy { SearchFragment() }
    private val notificationFragment by lazy { NotificationFragment() }
    private val profileFragment by lazy { ProfileFragment() }
    public lateinit var bottomNavigationView : BottomNavigationView
    private val firestore = Firebase.firestore
    private val gasosMessagingService = GasosMessagingService()

    private fun permissionCheck() {
        val preference = getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1000)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                if (isFirstCheck) {
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1000)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "위치 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        moveNotification(this)
        gasosMessagingService.myToken()
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        navigationBarController()
        permissionCheck()
    }

    private fun moveNotification(context: Context){
        val intentNotificated = Intent(context, NotificationActivity::class.java)
        Log.d("boho", intent.getStringExtra("notificated").toString())
        if(intent.getStringExtra("notificated")  == "true"){
            intentNotificated.putExtra("COstate",intent.getStringExtra("COstate"))
            intentNotificated.putExtra("LPGstte", intent.getStringExtra("LPGstate"))
            intentNotificated.putExtra("name", intent.getStringExtra("name"))
            startActivity(intentNotificated)
        }
    }

    private fun navigationBarController() {
        bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.home -> {
                        fragmentChanger(homeFragment)
                    }
                    R.id.search -> {
                        permissionCheck()
                        fragmentChanger(searchFragment)
                    }
                    // R.id.notification -> {
                      //  fragmentChanger(notificationFragment)
                    //}
                    R.id.profile -> {
                        fragmentChanger(profileFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    public fun fragmentChanger(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
    fun getHashKey(){
        var packageInfo : PackageInfo = PackageInfo()
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }

        for (signature: Signature in packageInfo.signatures){
            try{
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch(e: NoSuchAlgorithmException){
                Log.e("KEY_HASH", "Unable to get MessageDigest. signature = " + signature, e)
            }
        }
    }
}