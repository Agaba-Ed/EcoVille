package com.cleanworld.ecoville.ui

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cleanerworld.ecoville.R

class SearchResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent){
        if (Intent.ACTION_SEARCH == intent.action) {
            //Search action implementation!
        }

    }
}