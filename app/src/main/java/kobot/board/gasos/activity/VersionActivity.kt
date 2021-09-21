package kobot.board.gasos.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import kobot.board.gasos.R

class VersionActivity : AnimationActivity(TransitionMode.VERTICAL) {

    private lateinit var closeBtn : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_version)
        closeBtn = findViewById(R.id.version_close)
        closeBtn.setOnClickListener {
            finish()
        }
    }
}
