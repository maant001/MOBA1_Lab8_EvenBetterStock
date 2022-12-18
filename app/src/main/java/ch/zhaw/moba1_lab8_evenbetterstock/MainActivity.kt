package ch.zhaw.moba1_lab8_evenbetterstock

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import ch.zhaw.moba1_lab8_evenbetterstock.R.id.*
import org.json.JSONObject


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var stockListView: ListView
    private lateinit var stockInputView: TextInputEditText

    private var stockArray: ArrayList<String> = ArrayList<String>()
    private var stockArrayForDisplay: ArrayList<String> = ArrayList<String>()


    // variables for requests
    private lateinit var queue: RequestQueue
    private var urlFirstPart = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
    private var urlSecondPart = "&apikey=<R97TTGEBZXRBP60R>"


    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stockListView = findViewById(R.id.stockListView)
        stockInputView = findViewById(R.id.stockInputView)

        // listener on buttons
        arrayOf<Button>(
            findViewById(addStockButton),
            findViewById(refreshButton),
            findViewById(clearButton),
        ).forEach {it.setOnClickListener(this)}
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                addStockButton -> addStock()
                refreshButton -> updateStocks()
                clearButton -> clearList()
            }
        }
    }

    // get input string
    private fun getSymbol(): String {
        return stockInputView.text.toString()
    }

    // adds stock to stocklist to be shown
    private fun addStock() {
        // add symbol to array
        stockArray.add(getSymbol())
        putStocks()

        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stockArray)
        stockListView.adapter = arrayAdapter

        stockInputView.setText("")
    }

    private fun putStocks() {
        setStockNameAndPrice()

        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stockArrayForDisplay)
        stockListView.adapter = arrayAdapter
    }

    private fun setStockNameAndPrice() {
        for (stock in stockArray) {
            request(stock)
        }
    }

    private fun updateStocks() {
        putStocks()
    }

    // empty array, update view
    private fun clearList() {
        stockArray.clear()
        stockArrayForDisplay.clear()
        updateStocks()
    }

    private fun request(stock: String) {
        var sym = stock
        var finalUrl = urlFirstPart + sym + urlSecondPart

        // define request
        queue = Volley.newRequestQueue(this)

        queue.add((StringRequest(
            com.android.volley.Request.Method.GET, finalUrl,
            { response ->
                val jsonObject = JSONObject(response);
                val globalQuote = jsonObject.getJSONObject("Global Quote");
                val price = globalQuote.getString("05. price");
                stockArrayForDisplay.add(stock + "               " + price.toDouble().toString());

//                val arrayAdapter: ArrayAdapter<*>
//                arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stockArrayForDisplay)
//                stockListView.adapter = arrayAdapter
            },
            { error ->
                //Log.e("Volley", error.toString());
            }
        ))

        )
    }


}