package kobot.board.gasos.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kobot.board.gasos.R
import kobot.board.gasos.activity.MainActivity
import kobot.board.gasos.activity.MainNotificationActivity
import kobot.board.gasos.adaptor.MainRvAdapter
import kobot.board.gasos.data.Protected
import kobot.board.gasos.util.FireStoreManager
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.w3c.dom.Text
import java.net.URL

class HomeFragment : Fragment() {

    private lateinit var bohoListView : RecyclerView
    private lateinit var statusBtn : Button
    private var name : String? = null
    private var email : String? = null
    private var uid : String? = null
    private var photourl : URL? = null
    private lateinit var userName : TextView
    private lateinit var userEmail : TextView
    private lateinit var userProfile : CircleImageView

    private lateinit var protectedList : ArrayList<Protected>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                name = profile.displayName
                email = profile.email
                uid = profile.uid
                photourl = URL(profile.photoUrl.toString())
            }
        }

        var v = inflater.inflate(R.layout.fragment_home, container, false)
        val intent = Intent(v.context, MainNotificationActivity::class.java)

        CoroutineScope(Dispatchers.Main).launch {

            val deferred =
                runBlocking {
                    async {
                        val firestoreManager = FireStoreManager(name.toString())
                        firestoreManager.loadProtectedData()
                    }
                }
            delay(1000L)
            bohoListView = v.findViewById(R.id.bohoListView)
            val mAdapter = MainRvAdapter(v.context, deferred.await())
            protectedList = deferred.await()
            Log.d("protectedList", protectedList.toString())
            bohoListView.adapter = mAdapter

            val lm = LinearLayoutManager(v.context)
            bohoListView.layoutManager = lm
            bohoListView.setHasFixedSize(true)
        }

        statusBtn = v.findViewById(R.id.statusBtn)
        statusBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container, NotificationFragment())?.commit()
            (activity as MainActivity).bottomNavigationView.selectedItemId = R.id.notification
        }
        userName = v.findViewById(R.id.username)
        userName.text = name.toString()
        userEmail = v.findViewById(R.id.useremail)
        email = email.toString()
        userProfile = v.findViewById(R.id.userproile)
        userProfile.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container, ProfileFragment())?.commit()
            (activity as MainActivity).bottomNavigationView.selectedItemId = R.id.profile
        }
        return v
    }

}