package kobot.board.gasos.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kobot.board.gasos.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var client : GoogleSignInClient
    private lateinit var loginBtn : SignInButton
    fun setGoogleLogin(){
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, options)
        loginBtn.setOnClickListener {
            Log.d("wae-02", "111111111111111111111111111111111")
            startActivityForResult(client.signInIntent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.d("wae-03", task.result.idToken)
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken)
            }catch (e: ApiException){
                Log.d("wae-04", "안되지?!")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential =  GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful){
                    updateUI()
                }
            }
    }

    private fun updateUI(){
        val user = FirebaseAuth.getInstance().currentUser
        val email : String = auth.currentUser?.email.toString()
        val name = user!!.displayName
        val photourl = user.photoUrl.toString()

        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("name", name)
        intent.putExtra("photourl", photourl)

        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        loginBtn = findViewById<SignInButton>(R.id.google_login)
        setGoogleLogin()
    }
}