package stan.aac.experience.presentation.module.fandom.search

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import stan.aac.experience.foundation.entity.Fandom
import stan.aac.experience.implementation.module.fandom.search.FandomSearchViewModel
import stan.aac.experience.implementation.util.platform.android.lifecycle.viewModel

class FandomSearchFragment : Fragment() {
    private var recyclerView: RecyclerView? = null

    private fun onCreateView(context: Context): View {
        val result = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            background = ColorDrawable(Color.BLACK)
            orientation = LinearLayout.VERTICAL
        }
        val editText = EditText(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
//            background = ColorDrawable(Color.GREEN)
            setTextColor(Color.WHITE)
        }
        result.addView(editText)
        val recyclerView = RecyclerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutManager = LinearLayoutManager(context)
//            background = ColorDrawable(Color.RED)
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
        outState.putInt("test", 1)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = requireNotNull(context)
        val recyclerView = requireNotNull(recyclerView)
//        val data = (1..10).map { 2.0.pow(it).toInt() }
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
                holder.textView.text = data[position].toString()
            }
        }
        recyclerView.adapter = adapter
//        adapter.notifyDataSetChanged()
        val observer = Observer<List<Fandom>> {
            data = it.orEmpty()
            adapter.notifyDataSetChanged()
        }
        val viewModel = viewModel<FandomSearchViewModel>()
        viewModel.fandoms.observe(viewLifecycleOwner, observer)
        if (savedInstanceState != null) return
        viewModel.requestFandoms()
    }
}
