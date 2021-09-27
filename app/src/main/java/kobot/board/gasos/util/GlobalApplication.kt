package kobot.board.gasos.util

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "c7d9c4c4363a124e1965260f5c0de841")
    }
}