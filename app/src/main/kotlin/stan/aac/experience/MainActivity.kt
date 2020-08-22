package stan.aac.experience

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import stan.aac.experience.implementation.util.platform.android.lifecycle.reactive.subscribe
import stan.aac.experience.implementation.util.platform.android.lifecycle.reactive.toAction
import stan.aac.experience.presentation.module.fandom.search.FandomSearchFragment
import stan.aac.experience.presentation.module.start.StartFragment

class MainActivity : AppCompatActivity() {
    companion object {
        private val containerViewId = View.generateViewId() // todo
    }

    init {
        lifecycle.subscribe(StartFragment.subjectOutConsumer toAction {
            when (it) {
                is StartFragment.Broadcast.Out.Search -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(containerViewId, FandomSearchFragment())
                        commitNow()
                    }
                }
            }
        })
    }

//    private val containerViewId = View.generateViewId() // todo
//    private val containerViewId = 1 // todo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            background = ColorDrawable(Color.RED)
        }
        contentView.id = containerViewId
        setContentView(contentView)
        if (savedInstanceState != null) return
        supportFragmentManager.beginTransaction().apply {
//            replace(contentView.id, FandomSearchFragment())
            replace(containerViewId, StartFragment())
            commitNow()
        }
    }
}
