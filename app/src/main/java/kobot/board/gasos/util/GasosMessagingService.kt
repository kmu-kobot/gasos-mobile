package kobot.board.gasos.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kobot.board.gasos.R
import kobot.board.gasos.activity.MainActivity
import kobot.board.gasos.activity.NotificationActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URL

class GasosMessagingService : FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            Log.d("msgaaa", "gasos fcm activated")
            var title = it.title
            var message = it.body.toString()
            sendNotification(title, message)
        }
    }

    @SuppressLint("ServiceCast")
    private fun sendNotification(title: String?, body: String) {
        Log.d("msgaaa", "gasos fcm activated2")
        val intentMSG = Intent(applicationContext, NotificationActivity::class.java)

        val user = Firebase.auth.currentUser
        var name : String = ""
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                name = profile.displayName.toString()
            }
        }

        var bohoname = body.split(" ")[0]
        Log.d("boho", bohoname)
        CoroutineScope(Dispatchers.Main).launch{
            var protected = FireStoreManager(name).loadProtectedPersonalData(bohoname)

            Log.d("boho", protected.toString())
            if(protected?.stateLogList?.size != 0){
                Log.d("boho", protected.toString())
                intentMSG.putExtra("COstate", protected?.stateLogList!![protected?.stateLogList?.size!! - 1]["COstate"].toString())
                intentMSG.putExtra("LPGstate", protected?.stateLogList!![protected?.stateLogList?.size!! - 1]["LPGstate"].toString())
            }else{
                Log.d("boho", protected!!.stateLogList.toString())
                intentMSG.putExtra("COstate", protected?.stateLogList!![0]["COstate"].toString())
                intentMSG.putExtra("LPGstate", protected?.stateLogList!![0]["LPGstate"].toString())
            }
            delay(2000L)

            intentMSG.putExtra("name", bohoname)
            intentMSG.putExtra("notificated", "true")

            Log.d("제발", intentMSG.getStringExtra("notificated").toString())



            intentMSG.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intentMSG, PendingIntent.FLAG_ONE_SHOT)
            val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(applicationContext, "Channel ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel("ㅇㅇ", "hi", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val user = Firebase.auth.currentUser
        var name : String = ""
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                name = profile.displayName.toString()
            }
        }
        sendToken(name, token)
    }

    private fun sendToken(uid : String, token : String){
        val newToken = hashMapOf(
            "token" to token
        )
        val DB = Firebase.firestore
        DB.collection("Manager_"+uid)
            .document("FCM_Token")
            .set(newToken)
            .addOnSuccessListener {
                Log.d("newtoken", "newToken is "+token)
            }
    }

    public fun myToken() {
        //쓰레드 사용할것
        Thread(Runnable {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("token", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result.toString()
                onNewToken(token)
            })
        }).start()
    }
}