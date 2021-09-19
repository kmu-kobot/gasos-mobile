package kobot.board.gasos.data

import androidx.lifecycle.MutableLiveData

data class ResultSearchAddress(
    var documents: ArrayList<Place>
)

data class Place(
    var address_name : String,
    var address_type : String,
    var x : String,
    var y : String,
)