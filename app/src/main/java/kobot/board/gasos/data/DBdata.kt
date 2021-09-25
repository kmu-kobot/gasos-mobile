package kobot.board.gasos.data

data class Manager(
    var id: String = "",
    var name: String = ""
)

data class Protected(
    var name : String = "",
    var age : Int = 0,
    var address : String = "",
    var x : Double = -0.0,
    var y : Double = -0.0,
    var stateLogList : ArrayList<HashMap<Any, Any>>? = null
)

data class StateLog(
    var LPGstate : String = "",
    var COstate : String = "",
)
