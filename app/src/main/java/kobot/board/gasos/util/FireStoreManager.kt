package kobot.board.gasos.util

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kobot.board.gasos.data.Protected
import kobot.board.gasos.data.StateLog

class FireStoreManager(uid : String) {
    private val DB = Firebase.firestore
    private val uid = uid

    public suspend fun loadProtectedData(): ArrayList<Protected> {
        val protectedList = arrayListOf<Protected>()
        DB.collection("Manager_"+uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    Log.d("protectedList", document.id)
                    protectedList.add(Protected(name=document["name"] as String, document["age"].toString().toInt(), address= document["address"] as String, x=document["x"].toString().toDouble(), y=document["y"].toString().toDouble(), stateLogList = document["stateLogList"] as ArrayList<HashMap<Any, Any>>))
                    Log.d("protectedList",protectedList.toString())
                }
            }
        return protectedList
    }

    public fun loadProtectedPersonalData(protectd : String): Protected? {
        var protectedPerson : Protected? = null
        DB.collection("Manager_"+uid)
            .document("Protected_"+protectd)
            .get()
            .addOnCompleteListener { result ->
                if(result.isSuccessful){
                    Log.d("protectedList", "")
                    protectedPerson = result.result?.toObject(Protected::class.java)!!
                }
            }
        return protectedPerson
    }

    public fun insertProtectedData(protected: Protected){
        DB.collection("Manager_"+uid)
            .document("Protected_"+protected.name)
            .set(protected)
            .addOnSuccessListener {
                Log.d("ADD", "Manager_"+uid+" -> Protected_"+protected.name+" : ")
            }
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