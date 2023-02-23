package com.practicum.playlistmaker


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView


class SearchActivity : AppCompatActivity() {
    private var searchTextValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backFromSearch = findViewById<View>(R.id.searchBackButton)
        val inputEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener{
            inputEditText.setText("")
            hideKeyboard()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //empty
        }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchTextValue = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }



        backFromSearch.setOnClickListener{
            val backSearchIntent = Intent(this, MainActivity::class.java)
            startActivity(backSearchIntent)
            finish()
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchTextValue", searchTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextValue = savedInstanceState.getString("searchTextValue") ?: ""
    }
    private fun clearButtonVisibility(s:CharSequence?): Int{
        return if(s.isNullOrEmpty()) {
            View.GONE
        }else{
            View.VISIBLE
        }
    }
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}