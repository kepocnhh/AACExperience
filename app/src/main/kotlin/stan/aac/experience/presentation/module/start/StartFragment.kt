package stan.aac.experience.presentation.module.start

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import stan.aac.experience.implementation.util.reactive.subject.Subject
import stan.aac.experience.implementation.util.reactive.subject.SubjectConsumer
import stan.aac.experience.implementation.util.reactive.subject.publishSubject

class StartFragment : Fragment() {
    class Broadcast {
        init {
            error("No instance!")
        }

        sealed class Out {
            object Search : Out()
        }
    }

    companion object {
        private val subjectOut: Subject<Broadcast.Out> = publishSubject()
        val subjectOutConsumer: SubjectConsumer<Broadcast.Out> = subjectOut
    }

    private fun onCreateView(context: Context): View {
        val result = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            background = ColorDrawable(Color.BLACK)
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val buttonSearch = Button(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text = "search"
            gravity = Gravity.CENTER
        }
        buttonSearch.setOnClickListener {
            subjectOut next Broadcast.Out.Search
        }
        result.addView(buttonSearch)
        return result
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return onCreateView(requireNotNull(inflater.context))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
    }
}
