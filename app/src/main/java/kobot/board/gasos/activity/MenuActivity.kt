package kobot.board.gasos.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kobot.board.gasos.R
import kobot.board.gasos.adaptor.MainRvAdapter
import kobot.board.gasos.adaptor.SettingRvAdapter
import kobot.board.gasos.data.Protected
import kobot.board.gasos.util.FireStoreManager
import kotlinx.coroutines.*

class MenuActivity : AnimationActivity(TransitionMode.VERTICAL) {

    private lateinit var closeBtn : ImageButton
    private lateinit var bohoListView : RecyclerView
    private lateinit var protectedList : ArrayList<Protected>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        closeBtn = findViewById(R.id.menu_close)
        closeBtn.setOnClickListener {
            finish()
        }
        val user = Firebase.auth.currentUser
        var name : String? = ""
        var email : String?
        var uid : String?
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                name = profile.displayName
                email = profile.email
                uid = profile.uid
            }
        }
        CoroutineScope(Dispatchers.Main).launch {

            val deferred =
                runBlocking {
                    async {
                        val firestoreManager = FireStoreManager(name.toString())
                        firestoreManager.loadProtectedData()
                    }
                }
            delay(1000L)
            protectedList = deferred.await()
            bohoListView = findViewById(R.id.bohoList)
            val mAdapter = SettingRvAdapter(applicationContext, protectedList, name.toString())
            bohoListView.adapter = mAdapter

            val lm = LinearLayoutManager(applicationContext)
            bohoListView.layoutManager = lm
            bohoListView.setHasFixedSize(true)
        }
    }
}
