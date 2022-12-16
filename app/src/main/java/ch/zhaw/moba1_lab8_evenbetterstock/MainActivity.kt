package ch.zhaw.moba1_lab8_evenbetterstock

import android.annotation.SuppressLint
import android.os.Bundle
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


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var stockListView: ListView
    private lateinit var stockInputView: TextInputEditText

    private var stockArray: ArrayList<String> = ArrayList<String>()
    //private var stockArrayForDisplay: ArrayList<String> = ArrayList<String>()


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
        var tempSym = getSymbol()
        stockArray.add(tempSym)
        putStocks()
        stockInputView.setText("")
    }

    private fun putStocks() {
        //getStockNameAndPrice()

        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stockArray)
        stockListView.adapter = arrayAdapter

    }

    private fun setStockNameAndPrice() {
        //var tempArray = ArrayList<String>()

        for (stock in stockArray) {
            request(stock)
        }
    }

    // TODO button to update all stocks
    // for each -> request() in array?
    private fun updateStocks() {
        setStockNameAndPrice()
        putStocks()
    }

    // TODO reset/delete view
    // empty array, update view
    private fun clearList() {
        stockArray.clear()
        putStocks()
    }

    private fun request(stock: String){
        var sym = stock
        var finalUrl = urlFirstPart + sym + urlSecondPart

        // define request
        queue = Volley.newRequestQueue(this)

        // TODO check whats coming back from call here to extract price
        val request = StringRequest(
            Request.Method.GET, finalUrl,
            { response ->

                var strResp = response.toString()
                // textView
                var lines = strResp.replace(",", "").replace("\"", "").replace("{", "").replace("}", "").lines()
                var outputStock = lines.subList(5, 6).toString()

                //stockArrayForDisplay.add(outputStock)
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