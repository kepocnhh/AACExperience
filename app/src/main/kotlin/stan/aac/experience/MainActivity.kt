package stan.aac.experience

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = FrameLayout(this)
        val textView = TextView(this)
        textView.text = "hello stan.aac.experience"
        contentView.addView(textView)
        setContentView(contentView)
    }
}
