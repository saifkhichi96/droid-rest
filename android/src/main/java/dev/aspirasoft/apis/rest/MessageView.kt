package dev.aspirasoft.apis.rest

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dev.aspirasoft.apis.rest.models.Message

class MessageView(context: Context, attrs: AttributeSet?, incoming: Boolean) : LinearLayout(context, attrs) {

    constructor(context: Context) : this(context, null, false)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, false)

    private var messageSenderView: TextView
    private var messageBodyView: TextView

    init {
        val v = View.inflate(context, when {
            incoming -> R.layout.view_message_incoming
            else -> R.layout.view_message_outgoing
        }, null)
        v.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = when {
                incoming -> Gravity.START
                else -> Gravity.END
            }
        }

        this.addView(v)

        messageSenderView = findViewById(R.id.sender)
        messageBodyView = findViewById(R.id.body)
    }

    fun show(message: Message) {
        messageSenderView.text = message.sender
        messageBodyView.text = message.body
    }

}