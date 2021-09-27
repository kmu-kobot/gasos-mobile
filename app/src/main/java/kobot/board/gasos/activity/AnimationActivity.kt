package kobot.board.gasos.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kobot.board.gasos.R

abstract class AnimationActivity(
        private val transitionMode: TransitionMode = TransitionMode.NONE
    ) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (transitionMode) {
            TransitionMode.HORIZON_REVERSE -> overridePendingTransition(R.anim.horizon_enter_reverse, R.anim.none)
            TransitionMode.HORIZON -> overridePendingTransition(R.anim.horizon_enter, R.anim.none)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.vertical_enter, R.anim.none)
            else -> Unit
        }
    }

    override fun finish() {
        super.finish()

        when (transitionMode) {
            TransitionMode.HORIZON_REVERSE -> overridePendingTransition(R.anim.horizon_enter_reverse, R.anim.none)
            TransitionMode.HORIZON -> overridePendingTransition(R.anim.none, R.anim.horizon_exit)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.none, R.anim.vertical_exit)
            else -> Unit
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            when (transitionMode) {
                TransitionMode.HORIZON_REVERSE -> overridePendingTransition(R.anim.horizon_enter_reverse, R.anim.none)
                TransitionMode.HORIZON -> overridePendingTransition(R.anim.none, R.anim.horizon_exit)
                TransitionMode.VERTICAL -> overridePendingTransition(R.anim.none, R.anim.vertical_exit)
                else -> Unit
            }
        }
    }

    enum class TransitionMode {
        NONE,
        HORIZON,
        VERTICAL,
        HORIZON_REVERSE
    }
}
