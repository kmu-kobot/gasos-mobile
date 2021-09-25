package kobot.board.gasos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kobot.board.gasos.R
import kobot.board.gasos.adaptor.NotificationRvAdapter
import kobot.board.gasos.data.Protected

class NotificationFragment : Fragment() {

    private lateinit var notificationRecyclerView : RecyclerView

    var protectedList = arrayListOf<Protected>(
        Protected(name="김공낑", age=67, address="서울시 성북구 정릉로15길 41"),
        Protected(name="이낑강", age=61, address="서울시 성북구 보국문로11길 18-2"),
        Protected(name="오오옥", age=62, address="서울시 성북구 정릉로 77"),
        Protected(name="아아악", age=63, address="인천시 남동구 담방로21번길 23"),
        Protected(name="김공낑", age=67, address="서울시 성북구 동소문로2길 21"),
        Protected(name="이낑강", age=61, address="서울시 서초구 서초대로62길 26"),
        Protected(name="오오옥", age=62, address="서울시 강동구 천호대로 997"),
        Protected(name="아아악", age=63, address="경기 성남시 분당구 불정로 6"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_notification, container, false)
        val mAdapter = NotificationRvAdapter(v.context, protectedList)

        notificationRecyclerView = v.findViewById(R.id.notificationRecyclerView)
        notificationRecyclerView.adapter = mAdapter
        val lm = LinearLayoutManager(v.context)
        notificationRecyclerView.layoutManager = lm
        notificationRecyclerView.setHasFixedSize(true)

        return v
    }
}