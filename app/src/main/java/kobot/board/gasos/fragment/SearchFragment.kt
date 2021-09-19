package kobot.board.gasos.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import kobot.board.gasos.R
import kobot.board.gasos.data.Place
import kobot.board.gasos.data.ResultSearchAddress
import kobot.board.gasos.util.KakaoAPI
import net.daum.android.map.coord.MapCoord
import net.daum.android.map.coord.MapCoordLatLng
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.Key
import kotlin.math.roundToInt

class SearchFragment : Fragment() {

    private lateinit var mapview : MapView
    private lateinit var fineLocationBtn : ImageButton
    private lateinit var menuList : ImageButton
    private lateinit var zoomInBtn : ImageButton
    private lateinit var zoomOutBtn : ImageButton
    private lateinit var searchBtn : ImageButton
    private lateinit var searchBar : EditText
    private lateinit var bohoMenu : ConstraintLayout
    private lateinit var closeBtn : ImageButton

    companion object {
        const val KAKAO_BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK b288f99191ea0df97c6cd15310c87332"
    }

    private fun searchAddress(address: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)
        val call = api.getSearchAddress(API_KEY, address)
        var address = ArrayList<Place>()

        call.enqueue(object : Callback<ResultSearchAddress>{
            override fun onResponse(
                call: Call<ResultSearchAddress>,
                response: Response<ResultSearchAddress>
            ) {
                address = response.body()!!.documents
                Log.d("Test", "Raw : ${response.raw()}")
                Log.d("Test", "Body : ${response.body()}")
                Log.d("Test", "hi : ${address[0].x.toDouble()}")
                Log.d("Test", "hi : ${address[0].y.toDouble()}")
                if(address[0].x != null && address[0].y != null){
                    var x = (address[0].x.toFloat()*100000).roundToInt() / 100000f
                    var y = (address[0].y.toFloat()*100000).roundToInt() / 100000f
                    setLocation(x.toDouble(), y.toDouble())
                }
                else{
                    Toast.makeText(context, "주소 정보를 정확히 기입해주십시오.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultSearchAddress>, t: Throwable) {
                Log.w("Test_Failed", "통신 실패 : ${t.message}")
            }
        })
    }

    private var checker = 1

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)

        bohoMenu = v.findViewById(R.id.boho_menu)
        closeBtn = v.findViewById(R.id.menu_close)
        closeBtn.setOnClickListener {
            closeBohoMenu(v)
        }
        menuList = v.findViewById(R.id.menu_list)
        menuList.setOnClickListener {
            openBohoMenu(v)
        }
        zoomInBtn = v.findViewById(R.id.zoomInBtn)
        zoomInBtn.setOnClickListener {
            zoomIn()
        }

        zoomOutBtn = v.findViewById(R.id.zoomOutBtn)
        zoomOutBtn.setOnClickListener {
            zoomOut()
        }

        searchBtn = v.findViewById(R.id.search_btn)
        searchBtn.setOnClickListener {
            searchAddress(searchBar.text.toString())
        }
        searchBar = v.findViewById(R.id.search_bar)
        searchBar.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE){
                searchAddress(searchBar.text.toString())
                handled = true
            }
            handled
        }

        fineLocationBtn = v.findViewById(R.id.fine_location_btn)
        mapview = v.findViewById(R.id.map_view)

        findFineLocation()
        fineLocationBtn.setOnClickListener {
            if(checker == 1){
                stopFindingFineLocation()
                fineLocationBtn.setImageResource(R.drawable.ic_baseline_my_location_24)
                checker = 0
            }else{
                findFineLocation()
                fineLocationBtn.setImageResource(R.drawable.checked_location)
                checker = 1
            }
        }
        return v
    }

    private fun openBohoMenu(v : View) {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 1000
        bohoMenu.visibility = View.VISIBLE
        bohoMenu.animation = animation

        fineLocationBtn.visibility = View.GONE
        zoomInBtn.visibility = View.GONE
        zoomOutBtn.visibility = View.GONE
    }

    private fun closeBohoMenu(v : View) {
        val animation = AlphaAnimation(1.0f, 0.0f)
        animation.duration = 1000
        bohoMenu.animation = animation
        bohoMenu.visibility = View.GONE

        fineLocationBtn.visibility = View.VISIBLE
        zoomInBtn.visibility = View.VISIBLE
        zoomOutBtn.visibility = View.VISIBLE
    }

    private fun zoomIn(){
        val zoomLevel = mapview.zoomLevel
        if (zoomLevel != 0)mapview.setZoomLevel(zoomLevel - 1, true)
    }

    private fun zoomOut(){
        val zoomLevel = mapview.zoomLevel
        if (zoomLevel != 15)mapview.setZoomLevel(zoomLevel + 1, true)
    }

    private fun setLocation(x: Double, y:Double){
        Log.d("why", "${x}")
        Log.d("why", "${y}")
        val mapXY = MapPoint.mapPointWithGeoCoord(y, x)
        mapview.setMapCenterPoint(mapXY, true)
    }

    private fun findFineLocation() {
        mapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        mapview.setZoomLevel(3, true)
    }

    private fun stopFindingFineLocation() {
        mapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }
}
