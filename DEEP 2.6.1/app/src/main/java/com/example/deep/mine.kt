package com.example.deep

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

class mine : AppCompatActivity(), TextWatcher {
    var recyclerView: RecyclerView? = null
    var findeditText: EditText? = null
    var adapter: Adapter? = null
    var title: ArrayList<String>? = null
    var title_image: ArrayList<String>? = null
    var title_desc: ArrayList<String>? = null
    var desc: ArrayList<String>? = null
    var parms: ArrayList<String>? = null
    var images: ArrayList<String>? = null
    var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)

        title = ArrayList()
        title_image = ArrayList()
        title_desc = ArrayList()
        desc = ArrayList()
        parms = ArrayList()
        images = ArrayList()

        findeditText = findViewById<EditText>(R.id.findeditText)
        findeditText?.addTextChangedListener(this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        adapter = Adapter(this, title!!, title_image!!, title_desc!!, desc!!, parms!!, images!!)
        recyclerView?.adapter = adapter

        //findeditText?.setText("")
    }

    private fun JsonDataFromAsset(fileName: String): String? {
        var json: String? = null
        json = try {
            val inputStream = assets.open(fileName)
            val sizeOfFile = inputStream.available()
            val bufferData = ByteArray(sizeOfFile)
            inputStream.read(bufferData)
            inputStream.close()
            String(bufferData, charset("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return json
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {
        text = findeditText?.text.toString()
        text = text?.replace("[ ,@#!№;$%:^?&*()_+=`~.,/?-]".toRegex(), "")
            ?.lowercase(Locale.getDefault())

        title?.clear()
        title_desc?.clear()
        title_image?.clear()
        desc?.clear()
        parms?.clear()
        images?.clear()

        try {
            val jsonResponse = JSONObject(JsonDataFromAsset("information.json"))
            val pp_minesArray = jsonResponse.getJSONArray("mines")

            for (i in 0 until pp_minesArray.length()) {
                var tmp1 = ""
                var tmp2 = ""
                val mineInfo = pp_minesArray.getJSONObject(i)
                if (text == "" || mineInfo.getString("title")
                        .replace("[ ,@#!№;$%:^?&*()_+=`~.,/?-]".toRegex(), "").lowercase(Locale.getDefault())
                        .contains(text!!)
                ) {
                    title?.add(mineInfo.getString("title"))
                    title_image?.add(mineInfo.getString("title_image"))
                    title_desc?.add(mineInfo.getString("title_desc"))
                    desc?.add(mineInfo.getString("desc"))

                    val parmsArray = mineInfo.getJSONArray("parms")
                    for (j in 0 until parmsArray.length()) {
                        val parm = parmsArray.getJSONObject(j)
                        tmp1 = tmp1 + parm.getString("name_parms") + parm.getString("parmetr") + "\n"
                    }
                    parms?.add(tmp1)

                    val imgArray = mineInfo.getJSONArray("img")
                    for (k in 0 until imgArray.length()) {
                        tmp2 = if (k == imgArray.length() - 1) {
                            tmp2 + imgArray.getString(k)
                        } else {
                            tmp2 + imgArray.getString(k) + " "
                        }
                    }
                    images?.add(tmp2)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        adapter?.notifyDataSetChanged()
    }
}
