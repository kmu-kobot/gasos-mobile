package kobot.board.gasos.adaptor

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.text.Layout
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

class NotificationRvAdapter(val context: Context, val protectdList : ArrayList<Protected>): RecyclerView.Adapter<NotificationRvAdapter.Holder>(){

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        val contents = itemView?.findViewById<TextView>(R.id.boho_detail)
        val itemLayout = itemView?.findViewById<ConstraintLayout>(R.id.notification_layout)

        fun bind(protected : Protected, context : Context){
            if (protected.name != "") contents?.text = "경고!! "+protected.name+" ("+protected.age+") 씨 댁에서 위험 신호가 감지되었습니다. 확인하시려면 클릭해주세요."
            itemView.setOnClickListener {
                itemLayout?.setBackgroundColor(Color.parseColor("#FFFFFF"))
                context.startActivity(Intent(context, NotificationActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,  viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.notification_rv_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(protectdList[position], context)
    }

    override fun getItemCount(): Int {
        return protectdList.size
    }
}