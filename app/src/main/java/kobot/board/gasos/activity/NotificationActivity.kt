package kobot.board.gasos.activity

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kobot.board.gasos.R
import kobot.board.gasos.fragment.HomeFragment
import kobot.board.gasos.fragment.NotificationFragment
import kobot.board.gasos.fragment.ProfileFragment
import kobot.board.gasos.fragment.SearchFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class NotificationActivity : AppCompatActivity() {

    lateinit var closeButton : ImageButton
    lateinit var fusecockControllBtn : Button
    lateinit var naviBtn : Button
    lateinit var call119Btn : Button
    lateinit var warnBtn : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        fusecockControllBtn = findViewById(R.id.fusecock_controll_btn)
        naviBtn = findViewById(R.id.navi_browse_btn)
        call119Btn = findViewById(R.id.call_119_btn)
        warnBtn = findViewById(R.id.warnBtn)
        closeButton = findViewById(R.id.close_btn)

        closeButton.setOnClickListener {
            finish()
        }
    }
}