package kobot.board.gasos.activity

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kobot.board.gasos.R
import kobot.board.gasos.fragment.HomeFragment
import kobot.board.gasos.fragment.NotificationFragment
import kobot.board.gasos.fragment.ProfileFragment
import kobot.board.gasos.fragment.SearchFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class NotificationActivity : AnimationActivity(TransitionMode.VERTICAL) {

    lateinit var closeButton : ImageButton
    lateinit var naviBtn : Button
    lateinit var call119Btn : Button
    lateinit var warnBtn : Button
    lateinit var coStatus : TextView
    lateinit var gasStatus : TextView
    lateinit var name : TextView
    lateinit var alert : TextView

    var COstatus : String = ""
    var LPGstatus : String = ""
    var bohoName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        name = findViewById(R.id.notification_protected_person)
        coStatus = findViewById(R.id.co_status)
        gasStatus = findViewById(R.id.gas_status)
        call119Btn = findViewById(R.id.btn_119)
        warnBtn = findViewById(R.id.warnBtn)
        closeButton = findViewById(R.id.close_btn)
        alert = findViewById(R.id.notification_alert)

        call119Btn.setOnClickListener{
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:119")
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
        }

        if(intent.hasExtra("name")){
            bohoName = intent.getStringExtra("name").toString()
            name.text = bohoName+"???"
        }

        if(intent.hasExtra("COstate") && intent.hasExtra("COstate") != null){
            COstatus = intent.getStringExtra("COstate").toString()

            if(COstatus.toInt() >= 20){
                coStatus.setTextColor(Color.parseColor("#F36161"))
                coStatus.text = "??????"
                COstatus = "??????"
            }else{
                coStatus.setTextColor(Color.parseColor("#7DEF82"))
                coStatus.text = "??????"
                COstatus = "??????"
            }
        }

        if(intent.hasExtra("LPGstate") && intent.hasExtra("LPGstate") != null){
            LPGstatus = intent.getStringExtra("LPGstate").toString()

            if(LPGstatus == "??????"){
                gasStatus.setTextColor(Color.parseColor("#F36161"))
                gasStatus.text = "??????"
            }else{
                gasStatus.setTextColor(Color.parseColor("#7DEF82"))
                gasStatus.text = "??????"
            }
        }

        name.text = intent.getStringExtra("name").toString()+"???"

        Log.d("HiNotification", name.text.toString()+" "+coStatus.text.toString()+" "+gasStatus.text.toString())

        if(LPGstatus == "??????" || COstatus == "??????"){
            if(LPGstatus == "??????"){
                alert.text = "??????????????? ?????? ??????"
                warnBtn.text = "??????"
                warnBtn.setBackgroundResource(R.drawable.oval_red)
            }else{
                alert.text = "CO????????? ?????? ??????"
                warnBtn.text = "??????"
                warnBtn.setBackgroundResource(R.drawable.oval_red)
            }
        }else{
            alert.text = "  "
            warnBtn.text = "??????"
            warnBtn.setBackgroundResource(R.drawable.oval_green)
        }

        closeButton.setOnClickListener {
            finish()
        }
    }
}