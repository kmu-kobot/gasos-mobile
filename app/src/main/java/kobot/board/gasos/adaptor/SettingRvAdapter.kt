package kobot.board.gasos.adaptor

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kobot.board.gasos.R
import kobot.board.gasos.activity.NotificationActivity
import kobot.board.gasos.data.Protected
import kobot.board.gasos.fragment.NotificationFragment
import kobot.board.gasos.util.FireStoreManager

class SettingRvAdapter(val context: Context, val protectdList : ArrayList<Protected>, uid : String): RecyclerView.Adapter<SettingRvAdapter.Holder>(){

    val firestoreManager = FireStoreManager(uid)

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val name = itemView?.findViewById<TextView>(R.id.bohoname)
        val age = itemView?.findViewById<TextView>(R.id.bohoage)
        val address = itemView?.findViewById<TextView>(R.id.bohoaddress)

        fun bind(protected : Protected, context : Context, p0 : Int){
            if (protected.name != "") name?.text = protected.name
            if (protected.age != null) age?.text = "("+protected.age.toString()+")"
            if (protected.address != null)  address?.text = protected.address

            itemView.setOnClickListener {
                notifyItemRemoved(p0)
                protectdList.removeAt(p0)
                notifyItemChanged(p0, protectdList.size)
                firestoreManager.deleteProtectedData(name?.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,  viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.setting_rv_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(protectdList[position], context, position)
    }

    override fun getItemCount(): Int {
        return protectdList.size
    }
}