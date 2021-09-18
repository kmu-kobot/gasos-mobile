package kobot.board.gasos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import kobot.board.gasos.R
import net.daum.mf.map.api.MapView

class SearchFragment : Fragment() {
    lateinit var mapview : RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        mapview = v.findViewById(R.id.map_view)
        val kakaoMap = MapView(v.context)
        mapview.addView(kakaoMap)
        return v

    }
}