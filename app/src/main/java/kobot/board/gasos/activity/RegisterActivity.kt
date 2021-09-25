 package kobot.board.gasos.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kobot.board.gasos.R
import kobot.board.gasos.data.Protected
import kobot.board.gasos.util.FireStoreManager
import java.net.URL

 class RegisterActivity : AnimationActivity(TransitionMode.VERTICAL) {

    private lateinit var closeBtn : Button
    private lateinit var name_editText : EditText
    private lateinit var age_editText : EditText
    private lateinit var IPaddress_editText : EditText
    private lateinit var address_textView : TextView
    private lateinit var detailAddr_editText : EditText
    private lateinit var registerBtn : Button
    private lateinit var address : String
    private var age : Int = 0
    private var name : String = ""
    private var managerName : String = ""
    private var stateLogList = ArrayList<HashMap<Any, Any>>()


    override fun onCreate(savedInstanceState: Bundle?) {

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                managerName = profile.displayName.toString()
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        closeBtn = findViewById(R.id.register_close)
        closeBtn.setOnClickListener {
            finish()
        }
        name_editText = findViewById(R.id.editText)
        age_editText = findViewById(R.id.editText2)
        address_textView = findViewById(R.id.textView9)
        if(intent.hasExtra("address")){
            address = intent.getStringExtra("address").toString()
            address_textView.text = address
        }
        else{
            address = "주소가 제대로 입력되지 않았습니다."
            address_textView.text = address
        }
        registerBtn = findViewById(R.id.register_btn)
        registerBtn.setOnClickListener {
            if(name_editText.text != null && age_editText != null){
                name = name_editText.text.toString()
                age = age_editText.text.toString().toInt()
                postMainServer(managerName, name, age, address)
            }else{
                Toast.makeText(this, "정보를 모두 입력해 주십시오.", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun postMainServer(managerName: String, name: String, age: Int, address: String) {
        val firestoreManager = FireStoreManager(managerName)
        Log.d("post", name+" "+age.toString()+" "+address)
        var map = HashMap<Any, Any>()
        map.put("COstate", "")
        map.put("LPGstate", "")
        map.put("Time", Timestamp.now())
        stateLogList.add(map)
        firestoreManager.insertProtectedData(Protected(name=name, age=age, address=address, stateLogList = stateLogList))
        finish()
    }
}
