package kobot.board.gasos.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kobot.board.gasos.R
import kobot.board.gasos.activity.MainActivity
import kobot.board.gasos.activity.MainNotificationActivity

class HomeFragment : Fragment() {

    lateinit var bohoListView : RecyclerView
    lateinit var statusBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_home, container, false)
        val intent = Intent(v.context, MainNotificationActivity::class.java)
        bohoListView = v.findViewById(R.id.bohoListView)
        statusBtn = v.findViewById(R.id.statusBtn)
        statusBtn.setOnClickListener {
            startActivity(intent)
        }
        return v
    }

}