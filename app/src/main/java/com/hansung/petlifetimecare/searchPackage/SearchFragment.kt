package com.hansung.petlifetimecare.searchPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.hansung.petlifetimecare.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    lateinit var recyclerView:RecyclerView
    lateinit var welcomeTextView : TextView
    lateinit var messageEditText : EditText
    lateinit var sendButton: ImageButton
    var messageList: MutableList<Message>? = null
    lateinit var messageAdapter: MessageAdapter
    private val JSON = "application/json; charset=utf-8".toMediaType()

    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
        .readTimeout(30, TimeUnit.SECONDS) // read timeout
        .writeTimeout(30, TimeUnit.SECONDS) // write timeout
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chatgpt, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        welcomeTextView = view.findViewById(R.id.welcome_text)
        messageEditText = view.findViewById(R.id.massage_edit_text)
        sendButton = view.findViewById(R.id.send_btn)
        messageList = mutableListOf()

        messageAdapter = MessageAdapter(messageList!!)
        recyclerView.adapter = messageAdapter
        val lim = LinearLayoutManager(requireContext())
        lim.stackFromEnd = true
        recyclerView.layoutManager = lim




        sendButton.setOnClickListener{
            val question : String = messageEditText.getText().toString().trim()
            addToChat(question,Message.SENT_BY_ME)
            messageEditText.setText("")
            callAPI(question)
            welcomeTextView.visibility = View.GONE
        }



        return view
    }

    fun addResponse(response: String){
        messageList?.removeAt(messageList!!.size - 1)
        addToChat(response,Message.SENT_BY_BOT)
    }

    fun addToChat(message: String, sentBy: String) {
        activity?.runOnUiThread {
            messageList?.add(Message(message, sentBy))
            messageAdapter.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
        }

    }


    fun callAPI(question: String) {
        messageList?.add(Message("답변 하는중...",Message.SENT_BY_BOT))

        val modifiedQuestion = "$question\n- Please answer no more than 3 sentences, Give me a dog consultation"
        val jsonBody = JSONObject().apply {
            put("model", "text-davinci-003")
            put("prompt", modifiedQuestion)
            put("max_tokens", 300)
            put("temperature", 0.5)
        }

        val body = jsonBody.toString().toRequestBody(JSON)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization","Bearer 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")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
              if(response.isSuccessful){
                  try {
                      val jsonObject = JSONObject(response.body?.string() ?: "")
                      val jsonArray = jsonObject.getJSONArray("choices")
                      val result = jsonArray.getJSONObject(0).getString("text").trim() // trim to remove leading and trailing spaces
                      val cleanResult = result.replace("\\s+".toRegex(), " ") // replace consecutive spaces with a single space
                      addResponse(cleanResult)

                  } catch (e: JSONException) {
                      e.printStackTrace()
                  }

              }else{
                  addResponse("Failed to load response, HTTP code: ${response.code}, message: ${response.message}")

              }
            }

            override fun onFailure(call: Call, e: IOException) {
                addResponse("Failed to load response due to "+e.message)
            }
        })

    }



}