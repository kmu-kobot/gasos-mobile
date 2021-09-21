package kobot.board.gasos.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kobot.board.gasos.R
import kobot.board.gasos.activity.MainActivity
import kobot.board.gasos.activity.MainNotificationActivity
import org.w3c.dom.Text
import java.net.URL

class HomeFragment : Fragment() {

    private lateinit var bohoListView : RecyclerView
    private lateinit var statusBtn : Button
    private var name : String? = null
    private var email : String? = null
    private var photourl : URL? = null
    private lateinit var userName : TextView
    private lateinit var userEmail : TextView
    private lateinit var userProfile : CircleImageView

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
                photourl = URL(profile.photoUrl.toString())
            }
        }

        var v = inflater.inflate(R.layout.fragment_home, container, false)
        val intent = Intent(v.context, MainNotificationActivity::class.java)
        bohoListView = v.findViewById(R.id.bohoListView)
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