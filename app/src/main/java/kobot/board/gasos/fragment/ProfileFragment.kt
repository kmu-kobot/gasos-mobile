package kobot.board.gasos.fragment

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kobot.board.gasos.R
import kobot.board.gasos.activity.LoginActivity
import org.w3c.dom.Text
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ProfileFragment : Fragment() {

    private lateinit var userName : TextView
    private lateinit var userEmail : TextView
    private lateinit var userProfileImg : CircleImageView
    private lateinit var bohoBtn : Button
    private lateinit var deviceBtn : Button
    private lateinit var profileSettingsBtn : Button
    private var name : String? = null
    private var email : String? = null
    private var photourl : URL? = null

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }


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

        var v = inflater.inflate(R.layout.fragment_profile, container, false)

        userName = v.findViewById(R.id.user_name)
        userName.text = name as CharSequence

        userEmail = v.findViewById(R.id.user_email)
        userEmail.text = email as CharSequence

        userProfileImg = v.findViewById(R.id.user_profile_img)
        userProfileImg.setBackgroundResource(R.drawable.user)

        bohoBtn = v.findViewById(R.id.boho_daesangja)
        bohoBtn.setOnClickListener {
            showBohoList()
        }

        deviceBtn = v.findViewById(R.id.boho_daesangja_device)
        deviceBtn.setOnClickListener {
            showDeviceList()
        }

        profileSettingsBtn = v.findViewById(R.id.edit_profile)
        profileSettingsBtn.setOnClickListener {
            logOutCurrentUser()
        }

        return v
    }

    private fun logOutCurrentUser() {
        LoginActivity().Logout()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()
    }

    private fun showDeviceList() {

    }

    private fun showBohoList() {
    }
}