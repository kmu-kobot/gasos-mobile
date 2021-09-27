package kobot.board.gasos.adaptor

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kobot.board.gasos.R
import kobot.board.gasos.activity.NotificationActivity
import kobot.board.gasos.data.Protected
import kobot.board.gasos.fragment.NotificationFragment

class MainRvAdapter(val context: Context, val protectdList : ArrayList<Protected>): RecyclerView.Adapter<MainRvAdapter.Holder>(){

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val name = itemView?.findViewById<TextView>(R.id.bohoname)
        val age = itemView?.findViewById<TextView>(R.id.bohoage)
        val address = itemView?.findViewById<TextView>(R.id.bohoaddress)
        val statusLayout = itemView?.findViewById<ConstraintLayout>(R.id.boho_status_layout)
        val status = itemView?.findViewById<TextView>(R.id.boho_status)

        fun bind(protected : Protected, context : Context){


            if (protected.name != "") name?.text = protected.name
            if (protected.age != null) age?.text = "("+protected.age.toString()+")"
            if (protected.address != null)  address?.text = protected.address

            if(protected.stateLogList?.size !=0){
                val costate = protected.stateLogList!![protected.stateLogList?.size!! - 1]["COstate"]

                if(costate != ""){
                    if (costate.toString().toInt() >= 20 || protected.stateLogList!![protected.stateLogList?.size!! - 1]["LPGstate"] == "위험"){
                        statusLayout?.setBackgroundResource(R.drawable.oval_red)
                        status?.text = "위험"
                    }else{
                        statusLayout?.setBackgroundResource(R.drawable.oval_green)
                        status?.text = "안전"
                    }
                }
            }

            itemView.setOnClickListener {
                var intent = Intent(context, NotificationActivity::class.java)
                intent.putExtra("COstate", protected.stateLogList!![protected.stateLogList?.size!! - 1]["COstate"].toString())
                intent.putExtra("LPGstate", protected.stateLogList!![protected.stateLogList?.size!! - 1]["LPGstate"].toString())
                intent.putExtra("name", name?.text.toString())

                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,  viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(protectdList[position], context)
    }

    override fun getItemCount(): Int {
        return protectdList.size
    }
}