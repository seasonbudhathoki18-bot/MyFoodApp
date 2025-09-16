package com.example.myfoodapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    @Inject lateinit var apiService: ApiService

    private lateinit var adapter: DynamicEntityAdapter
    private val entityList = mutableListOf<Map<String, Any>>() // dynamic rows

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val keypass = intent.getStringExtra("keypass") ?: ""
        Log.d("KEYPASS_DEBUG", "DashboardActivity keypass: $keypass")

        // show keypass in header
        findViewById<TextView>(R.id.tvKeypass)?.text = "Keypass: $keypass"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = DynamicEntityAdapter(entityList) { entity ->
            val entityJson = Gson().toJson(entity)
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("entity_json", entityJson)
            intent.putExtra("keypass", keypass)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        fetchEntities(keypass)
    }

    private fun fetchEntities(keypass: String) {
        apiService.getDashboard(keypass).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (!response.isSuccessful) {
                        Toast.makeText(this@DashboardActivity, "API error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        Log.e("API_RESPONSE_DEBUG", "Non-200: ${response.errorBody()?.string()}")
                        return
                    }
                    val raw = response.body()?.string().orEmpty()
                    Log.d("API_RESPONSE_DEBUG", raw)

                    val list = parseFlexibleList(raw)
                    entityList.clear()
                    entityList.addAll(list)
                    adapter.notifyDataSetChanged()

                    if (entityList.isEmpty()) {
                        Toast.makeText(this@DashboardActivity, "No items found", Toast.LENGTH_SHORT).show()
                    }
                } catch (t: Throwable) {
                    Log.e("PARSE_ERROR", "Failed to parse dashboard JSON", t)
                    Toast.makeText(this@DashboardActivity, "Data error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("NETWORK_ERROR", "Dashboard request failed", t)
                Toast.makeText(this@DashboardActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Accepts:
     * - top-level array: [ {...}, {...} ]
     * - object with common wrappers: { "entities": [...], "items": [...], "data": [...] }
     * - single object: { ... } -> wrapped as a 1-item list
     */
    private fun parseFlexibleList(raw: String): List<Map<String, Any>> {
        if (raw.isBlank()) return emptyList()

        val je: JsonElement = try {
            // Legacy Gson parsing for older versions
            JsonParser().parse(raw)
        } catch (_: Throwable) {
            return emptyList()
        }

        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val gson = Gson()

        return when {
            je.isJsonArray -> gson.fromJson(je, type)
            je.isJsonObject -> {
                val obj: JsonObject = je.asJsonObject
                val candidates = listOf("entities", "items", "data", "list")
                val arr: JsonArray? = candidates
                    .asSequence()
                    .mapNotNull { k -> obj.getAsJsonArray(k) }
                    .firstOrNull()

                if (arr != null) {
                    gson.fromJson(arr, type)
                } else {
                    // If no array found, wrap the object itself
                    listOf(obj.entrySet().associate { it.key to it.value.toString() })
                }
            }
            else -> emptyList()
        }
    }

}
