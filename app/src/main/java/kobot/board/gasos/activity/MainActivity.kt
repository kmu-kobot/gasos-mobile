package kobot.board.gasos.activity

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kobot.board.gasos.R
import kobot.board.gasos.fragment.HomeFragment
import kobot.board.gasos.fragment.NotificationFragment
import kobot.board.gasos.fragment.ProfileFragment
import kobot.board.gasos.fragment.SearchFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {

    private val homeFragment by lazy { HomeFragment() }
    private val searchFragment by lazy { SearchFragment() }
    private val notificationFragment by lazy { NotificationFragment() }
    private val profileFragment by lazy { ProfileFragment() }
    lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        navigationBarController()
        getHashKey()
    }

    private fun navigationBarController() {
        bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.home -> {
                        fragmentChanger(homeFragment)
                    }
                    R.id.search -> {
                        fragmentChanger(searchFragment)
                    }
                    R.id.notification -> {
                        fragmentChanger(notificationFragment)
                    }
                    R.id.profile -> {
                        fragmentChanger(profileFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    private fun fragmentChanger(fragment: Fragment){
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