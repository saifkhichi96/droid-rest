package dev.aspirasoft.apis.rest

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import co.aspirasoft.apis.rest.HttpMethod
import co.aspirasoft.apis.rest.HttpTask
import co.aspirasoft.apis.rest.ResponseListener
import dev.aspirasoft.apis.rest.models.Message
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ResponseListener<Message> {

    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageAdapter = MessageAdapter(this, ArrayList())
        messagesList.adapter = messageAdapter
        sendButton.setOnClickListener {
            val messageContent = inputField.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                send(Message().apply {
                    this.sender = "You"
                    this.body = messageContent
                })
                inputField.text?.clear()
            }
        }
    }

    override fun onRequestSuccessful(response: Message) {
        messageAdapter.add(response)
        scrollToBottom()
    }

    override fun onRequestFailed(ex: Exception) {
        messageAdapter.add(Message().apply {
            this.sender = "System"
            this.body = ex.javaClass.simpleName + ": " + ex.message
        })
        scrollToBottom()
    }

    private fun send(message: Message) {
        messageAdapter.add(message)
        scrollToBottom()

        HttpTask.Builder<Message, Message>(Message::class.java)
                .setMethod(HttpMethod.POST)
                .setRequestUrl("demo-server-side.php")
                .setPayload(message)
                .build(WebServer)
                .startAsync(this)
    }

    private fun scrollToBottom() {
        messagesList.post {
            // Select the last row so it will scroll into view...
            messagesList.setSelection(messageAdapter.count - 1)
        }
    }

    private class MessageAdapter(context: Context, private val messages: List<Message>)
        : ArrayAdapter<Message>(context, -1, messages) {

        override fun add(message: Message?) {
            super.add(message)
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = MessageView(context, null, position % 2 != 0)
            view.show(messages[position])
            return view
        }

    }

}