package stan.aac.experience.presentation.module.fandom.search

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import stan.aac.experience.App
import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.implementation.module.fandom.search.FandomSearchViewModel
import stan.aac.experience.implementation.util.platform.android.lifecycle.coroutines.isBusyConsumer
import stan.aac.experience.implementation.util.platform.android.lifecycle.reactive.subscribe
import stan.aac.experience.implementation.util.platform.android.lifecycle.reactive.subscribeAll
import stan.aac.experience.implementation.util.platform.android.lifecycle.reactive.toAction
import stan.aac.experience.implementation.util.platform.android.lifecycle.viewModel
import stan.aac.experience.implementation.util.reactive.subject.Subject
import stan.aac.experience.implementation.util.reactive.subject.SubjectConsumer
import stan.aac.experience.implementation.util.reactive.subject.publishSubject

class FandomSearchFragment : Fragment() {
    class Broadcast {
        init {
            error("No instance!")
        }

        sealed class Out {
            object Back : Out()
        }
    }

    companion object {
        private val subjectOut: Subject<Broadcast.Out> = publishSubject()
        val subjectOutConsumer: SubjectConsumer<Broadcast.Out> = subjectOut
    }

    private var recyclerView: RecyclerView? = null
    private var editText: EditText? = null
    private var progressView: View? = null

    private fun onCreateView(context: Context): View {
        val result = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            background = ColorDrawable(Color.BLACK)
            orientation = LinearLayout.VERTICAL
        }
        val buttonBack = Button(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text = "back"
            gravity = Gravity.CENTER
        }
        buttonBack.setOnClickListener {
            subjectOut next Broadcast.Out.Back
        }
        val progressView = ProgressBar(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        this.progressView = progressView
        val toolbar: ViewGroup = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
        }
        toolbar.addView(buttonBack)
        toolbar.addView(progressView)
        result.addView(toolbar)
        val editText = EditText(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setTextColor(Color.WHITE)
        }
        this.editText = editText
        result.addView(editText)
        val recyclerView = RecyclerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutManager = LinearLayoutManager(context)
        }
        this.recyclerView = recyclerView
        result.addView(recyclerView)
        return result
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return onCreateView(requireNotNull(inflater.context))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("searchQuery", requireNotNull(editText).text.toString())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = requireNotNull(context)
        val recyclerView = requireNotNull(recyclerView)
        val editText = requireNotNull(editText)
        val progressView = requireNotNull(progressView)
        var data: List<Fandom> = emptyList()
        class ViewHolder : RecyclerView.ViewHolder(FrameLayout(context)) {
            val textView: TextView = TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setTextColor(Color.WHITE)
            }
            init {
                val root = itemView as ViewGroup
                root.apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                root.addView(textView)
            }
        }
        val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return ViewHolder()
            }

            override fun getItemCount(): Int {
                return data.size
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                check(holder is ViewHolder)
                holder.textView.text = data[position].name
            }
        }
        recyclerView.adapter = adapter
        val viewModel = viewModel<FandomSearchViewModel>(App.viewModelFactory)
        lifecycle.subscribe(viewModel.broadcastConsumer) {
            when (it) {
                is FandomSearchViewModel.Broadcast.Error -> {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = editText.text.toString()
                viewModel.requestFandoms(text)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        viewLifecycleOwner.lifecycle.subscribeAll(
            viewModel.dataConsumer toAction {
                when (it) {
                    is FandomSearchViewModel.Data.Fandoms -> {
                        data = it.list
                        adapter.notifyDataSetChanged()
                    }
                }
            },
            viewModel.isBusyConsumer() toAction {
                if (it.isBusy && progressView.visibility != View.VISIBLE) {
                    progressView.visibility = View.VISIBLE
                } else if (!it.isBusy && progressView.visibility != View.INVISIBLE) {
                    progressView.visibility = View.INVISIBLE
                }
            }
        )
        if (savedInstanceState == null) {
            progressView.visibility = View.INVISIBLE
            editText.addTextChangedListener(textWatcher)
            viewModel.requestFandoms(null)
        } else {
            val searchQuery = savedInstanceState.getString("searchQuery")
            if (searchQuery.isNullOrEmpty()) {
                // todo
            } else {
                editText.setText(searchQuery)
            }
            editText.addTextChangedListener(textWatcher)
        }
    }
}
