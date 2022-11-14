package ch.zhaw.moba1_lab8_evenbetterstock

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import ch.zhaw.moba1_lab8_evenbetterstock.R.id.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    //private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var stockInputView: TextInputEditText

    private lateinit var queue: RequestQueue
    //private lateinit var request: StringRequest
    private var urlFirstPart = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
    private var urlSecondPart = "&apikey=<R97TTGEBZXRBP60R>"


    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.stockListRecyclerView)
        stockInputView = findViewById(R.id.stockInputView)

        // listener on button
        arrayOf<Button>(
            findViewById(button3),
        ).forEach {it.setOnClickListener(this)}
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                button3 -> addStock()
            }
        }
    }

    private fun getSymbol(): String {
        return stockInputView.text.toString()
    }

    // TODO
    private fun addStock() {

        //get text from textinput
        var sym = getSymbol()
        var finalUrl = urlFirstPart + sym + urlSecondPart

        // define request
        queue = Volley.newRequestQueue(this)

        val request = StringRequest(
            Request.Method.GET, finalUrl,
            { response ->

                var strResp = response.toString()
                // textView
                var lines = strResp.replace(",", "").replace("\"", "").replace("{", "").replace("}", "").lines()
                var outputStock = lines.subList(2, lines.lastIndex)

                val arrayAdapter: ArrayAdapter<*>
                arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, outputStock)
                //recyclerView.adapter = arrayAdapter
            },
            {
                    error ->

                //textView!!.text = "That didn't work!"
                // Error
            }
        )
        // add call to request queue
        queue.add(request)
    }


}