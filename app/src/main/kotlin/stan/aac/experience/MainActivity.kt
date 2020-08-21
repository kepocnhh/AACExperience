package stan.aac.experience

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import stan.aac.experience.presentation.module.fandom.search.FandomSearchFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            background = ColorDrawable(Color.RED)
        }
        contentView.id = 1
        setContentView(contentView)
        if (savedInstanceState != null) return
        supportFragmentManager.beginTransaction().apply {
            replace(contentView.id, FandomSearchFragment())
            commitNow()
        }
    }
}
