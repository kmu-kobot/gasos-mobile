package kobot.board.gasos.util

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kobot.board.gasos.data.Protected
import kobot.board.gasos.data.StateLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FireStoreManager(uid : String) {
    private val DB = Firebase.firestore
    private val uid = uid

    public suspend fun loadProtectedData(): ArrayList<Protected> {
        val protectedList = arrayListOf<Protected>()
        DB.collection("Manager_"+uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    if(document.id != "FCM_Token"){
                        Log.d("protectedList", document.id)
                        protectedList.add(Protected(name=document["name"] as String, document["age"].toString().toInt(), address= document["address"] as String, x=document["x"].toString().toDouble(), y=document["y"].toString().toDouble(), stateLogList = document["stateLogList"] as ArrayList<HashMap<Any, Any>>))
                        Log.d("protectedList",protectedList.toString())
                    }
                }
            }
        return protectedList
    }

    public suspend fun loadProtectedPersonalData(protected : String): Protected? {
        var protectedPerson : Protected? = null

        try{
            DB.collection("Manager_"+uid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result){
                        if(document.id == "Protected_"+protected){
                            Log.d("protectedList", document.id)
                            protectedPerson = Protected(name=document["name"] as String, document["age"].toString().toInt(), address= document["address"] as String, x=document["x"].toString().toDouble(), y=document["y"].toString().toDouble(), stateLogList = document["stateLogList"] as ArrayList<HashMap<Any, Any>>)
                            Log.d("boho", "hi : "+protectedPerson)
                        }
                    }
                }
            delay(1000L)
            Log.d("boho", "hello : "+protectedPerson)
            return protectedPerson

        }catch (e : Exception){
            return protectedPerson
        }
    }

    public fun insertProtectedData(protected: Protected){
        DB.collection("Manager_"+uid)
            .document("Protected_"+protected.name)
            .set(protected)
            .addOnSuccessListener {
                Log.d("ADD", "Manager_"+uid+" -> Protected_"+protected.name+" : ")
            }
        
    }

    public fun insertNotificationData(name : String, title : String, body : String){

    }

    public fun deleteProtectedData(protected : String){
        DB.collection("Manager_"+uid)
            .document("Protected_"+protected)
            .delete()
            .addOnSuccessListener {
                Log.d("Delete", "Protected_"+protected+" is successfully deleted")
            }
    }
}
