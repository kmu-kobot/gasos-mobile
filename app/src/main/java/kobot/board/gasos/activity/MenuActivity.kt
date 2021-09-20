package kobot.board.gasos.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import kobot.board.gasos.R

class MenuActivity : AnimationActivity(TransitionMode.VERTICAL) {

    private lateinit var closeBtn : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        closeBtn = findViewById(R.id.menu_close)
        closeBtn.setOnClickListener {
            finish()
        }
    }
}
