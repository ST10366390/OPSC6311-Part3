package com.example.nomismaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class CategoriesActivity : AppCompatActivity() {
 private lateinit var binding: ActivityCategoriesBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_categories)
        databaseHelper = DatabaseHelper(this)

                binding.addBtn.setOnClickListener {
                    // Start NewCategoryActivity to add a new category
                    val intent = Intent(this, NewCategoryActivity::class.java)
                    startActivity(intent)
                }

                setupRecyclerView()
                loadCategories()
            }

            private fun setupRecyclerView() {
                adapter = CategoriesAdapter(emptyList())
                binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.categoriesRecyclerView.adapter = adapter
            }

            private fun loadCategories() {
                val categories = databaseHelper.getAllCategories()
                if (categories.isNotEmpty()) {
                    adapter.updateCategories(categories)
                } else {
                    Toast.makeText(this, "No categories found", Toast.LENGTH_SHORT).show()
                }
            }

            private class CategoriesAdapter(private var categories: List<String>) :
                RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

                fun updateCategories(newCategories: List<String>) {
                    categories = newCategories
                    notifyDataSetChanged()
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(android.R.layout.simple_list_item_1, parent, false)
                    return CategoryViewHolder(view)
                }

                override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
                    holder.bind(categories[position])
                }

                override fun getItemCount(): Int = categories.size

                class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                    private val textView: TextView = itemView.findViewById(android.R.id.text1)
                    fun bind(category: String) {
                        textView.text = category
                    }
                }
            }
        }
