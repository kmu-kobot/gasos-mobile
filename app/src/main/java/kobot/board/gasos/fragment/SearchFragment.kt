package kobot.board.gasos.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kobot.board.gasos.R
import kobot.board.gasos.activity.NotificationActivity
import kobot.board.gasos.activity.RegisterActivity
import kobot.board.gasos.adaptor.MapRvAdapter
import kobot.board.gasos.data.Place
import kobot.board.gasos.data.Protected
import kobot.board.gasos.data.ResultSearchAddress
import kobot.board.gasos.util.FireStoreManager
import kobot.board.gasos.util.KakaoAPI
import kotlinx.coroutines.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
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
    private lateinit var addressInfo : TextView
    private lateinit var dialog : RelativeLayout
    private lateinit var yesBtn : Button
    private lateinit var noBtn : Button
    private lateinit var marker : MapPOIItem
    private lateinit var eventListener : MarkerEventListener
    private lateinit var bohoList : ConstraintLayout
    private lateinit var protectedListView : RecyclerView
    private lateinit var close : ImageButton
    private var addressReg : String = ""
    private var name : String? = ""

    private lateinit var protectedList : ArrayList<Protected>

    companion object {
        const val KAKAO_BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK b288f99191ea0df97c6cd15310c87332"
    }

    private fun UpdateAddress(address: String, name : String){
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
                if(address[0].x != null && address[0].y != null){
                    var x = (address[0].x.toFloat()*100000).roundToInt() / 100000f
                    var y = (address[0].y.toFloat()*100000).roundToInt() / 100000f
                    marker = MapPOIItem()
                    marker
                    marker.apply {
                        marker.itemName = name
                        mapPoint = MapPoint.mapPointWithGeoCoord(y.toDouble(), x.toDouble())
                        markerType = MapPOIItem.MarkerType.YellowPin
                        selectedMarkerType = MapPOIItem.MarkerType.YellowPin
                        mapview.addPOIItem(marker)
                    }
                }
                else{
                }
            }

            override fun onFailure(call: Call<ResultSearchAddress>, t: Throwable) {
                Log.w("Test_Failed", "통신 실패 : ${t.message}")
            }
        })
    }

    private fun searchOnlyAddress(address: String){
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
               if(address[0].x != null && address[0].y != null){
                    var x = (address[0].x.toFloat()*100000).roundToInt() / 100000f
                    var y = (address[0].y.toFloat()*100000).roundToInt() / 100000f
                    setLocation(x.toDouble(), y.toDouble())
                }
                else{
                }
            }

            override fun onFailure(call: Call<ResultSearchAddress>, t: Throwable) {
                Log.w("Test_Failed", "통신 실패 : ${t.message}")
            }
        })
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
                    allowMarker(x.toDouble(), y.toDouble(), address[0].address_name)
                    addressReg = address[0].address_name
                }
                else{
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
        protectedListView = v.findViewById(R.id.protectedList)
        bohoList = v.findViewById(R.id.listmenu)
        close = v.findViewById(R.id.menuclose)
        close.setOnClickListener {
            val fadeAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation_from_bottom_to_top)
            bohoList.startAnimation(fadeAnim)
            bohoList.visibility = View.GONE
        }

        val user = Firebase.auth.currentUser
        var email : String?
        var uid : String?
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                name = profile.displayName
                email = profile.email
                uid = profile.uid
            }
        }

        CoroutineScope(Dispatchers.Main).launch {

            val deferred =
                runBlocking {
                    async {
                        val firestoreManager = FireStoreManager(name.toString())
                        firestoreManager.loadProtectedData()
                    }
                }
            delay(1000L)
            protectedList = deferred.await()
            val mAdapter = MapRvAdapter(v.context, protectedList)
            mAdapter.setItemClickListener(object : MapRvAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    val fadeAnim: Animation =
                        AnimationUtils.loadAnimation(context, R.anim.animation_from_bottom_to_top)
                    bohoList.startAnimation(fadeAnim)
                    bohoList.visibility = View.GONE

                    searchOnlyAddress(protectedList[position].address.toString())
                    stopFindingFineLocation()
                }

                override fun onClick(v: View?) {
                }

            })
            protectedListView.adapter = mAdapter
            val lm = LinearLayoutManager(v.context)
            protectedListView.layoutManager = lm
            protectedListView.setHasFixedSize(true)
            refreshMarkerData()
        }

        eventListener = MarkerEventListener(v.context)
        dialog = v.findViewById(R.id.marker_layout)

        addressInfo = v.findViewById(R.id.address_alert)
        yesBtn = v.findViewById(R.id.allow_marker_btn)
        yesBtn.setOnClickListener {
            val fadeAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation_from_bottom_to_top)
            dialog.startAnimation(fadeAnim)
            dialog.visibility = View.GONE
            addMarkerData(v.context)
        }
        noBtn = v.findViewById(R.id.deny_marker_btn)
        noBtn.setOnClickListener {
            val fadeAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation_from_bottom_to_top)
            dialog.startAnimation(fadeAnim)
            dialog.visibility = View.GONE
            mapview.removePOIItem(marker)
        }

        bohoMenu = v.findViewById(R.id.boho_menu)
        closeBtn = v.findViewById(R.id.menu_close)

        menuList = v.findViewById(R.id.menu_list)
        menuList.setOnClickListener {
            val fadeAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation_from_top_to_bottom)
            bohoList.startAnimation(fadeAnim)
            bohoList.visibility = View.VISIBLE

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
        mapview.setPOIItemEventListener(eventListener)

        findFineLocation()
        fineLocationBtn.setOnClickListener {
            if(checker == 1){
                stopFindingFineLocation()

            }else{
                findFineLocation()
            }
        }

        return v
    }

    private fun refreshMarkerData() {
        for (protected in protectedList){
            UpdateAddress(protected.address.toString(), protected.name.toString()+" ("+protected.age.toString()+")")
        }
    }

    private fun addMarkerData(context: Context) {
        val intent = Intent(context, RegisterActivity::class.java)
        intent.putExtra("address", addressReg)
        context.startActivity(intent)
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
        mapview.setZoomLevel(3, true)
    }

    private fun allowMarker(x: Double, y: Double, address: String) {
        marker = MapPOIItem()
        marker.apply {
            marker.itemName = address
            mapPoint = MapPoint.mapPointWithGeoCoord(y, x)
            markerType = MapPOIItem.MarkerType.YellowPin
            selectedMarkerType = MapPOIItem.MarkerType.YellowPin
        }
        addressInfo.text = address+"의 주소로 보호대상자를 등록하시겠습니까?"
        mapview.addPOIItem(marker)
        val fadeAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation_from_top_to_bottom)
        dialog.startAnimation(fadeAnim)
        dialog.visibility = View.VISIBLE

    }

    private fun findFineLocation() {
        mapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        mapview.setZoomLevel(3, true)
        fineLocationBtn.setImageResource(R.drawable.checked_location)
        checker = 1
    }

    private fun stopFindingFineLocation() {
        fineLocationBtn.setImageResource(R.drawable.ic_baseline_my_location_24)
        checker = 0
        mapview.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    class MarkerEventListener(val context: Context): MapView.POIItemEventListener{
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            p0?.setMapCenterPoint(p1?.mapPoint, true)
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
            val intent = Intent(context, NotificationActivity::class.java)
            val user = Firebase.auth.currentUser
            var name : String = ""
            user?.let {
                for (profile in it.providerData) {
                    // Id of the provider (ex: google.com)
                    name = profile.displayName.toString()
                }
            }
            var bohoname = p1!!.itemName.toString().split(" ")[0]
            CoroutineScope(Dispatchers.Main).launch{
                var protected = FireStoreManager(name).loadProtectedPersonalData(bohoname)

                Log.d("boho", protected.toString())
                if(protected?.stateLogList?.size != 0){
                    Log.d("boho", protected.toString())
                    intent.putExtra("COstate", protected?.stateLogList!![protected?.stateLogList?.size!! - 1]["COstate"].toString())
                    intent.putExtra("LPGstate", protected?.stateLogList!![protected?.stateLogList?.size!! - 1]["LPGstate"].toString())
                }else{
                    Log.d("boho", protected!!.stateLogList.toString())
                    intent.putExtra("COstate", protected?.stateLogList!![0]["COstate"].toString())
                    intent.putExtra("LPGstate", protected?.stateLogList!![0]["LPGstate"].toString())
                }
                intent.putExtra("name", bohoname)
                context.startActivity(intent)
            }
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {
        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        }

    }
}
