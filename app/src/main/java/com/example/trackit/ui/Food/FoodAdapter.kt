package com.example.trackit.ui.Nutrition

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.ui.theme.AndroidGreen

class FoodAdapter(
    foodList: MutableList<FoodData>,
    val context: Context,
    val onFoodItemClickListener: OnFoodItemClickListener) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    interface OnFoodItemClickListener {
        fun onFoodItemClick(food: FoodData)
    }

    private var filteredList: MutableList<FoodData> = mutableListOf()
    private var searchQuery: String = ""

    init {
        filteredList.addAll(foodList)
    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameFood: TextView = itemView.findViewById(R.id.name_food)
        private val protein: TextView = itemView.findViewById(R.id.protein_text)
        private val fat: TextView = itemView.findViewById(R.id.fat_text)
        private val carbs: TextView = itemView.findViewById(R.id.carbs_text)
        private val calories: TextView = itemView.findViewById(R.id.calories_text)

        fun bind(food: FoodData) {
            nameFood.text = food.name
            protein.text = food.protein.toString()
            fat.text = food.fat.toString()
            carbs.text = food.carbs.toString()
            calories.text = food.calories.toString()

            val AndroidGreen = Color.parseColor("#99CD4E")

            // Проверяем, совпадает ли поисковый запрос с именем еды
            val highlightedText = if (searchQuery.isNotEmpty()) {
                val startIndex = food.name.indexOf(searchQuery, ignoreCase = true)
                if (startIndex != -1) {
                    val endIndex = startIndex + searchQuery.length
                    val spannableString = SpannableString(food.name)
                    spannableString.setSpan(
                        ForegroundColorSpan(AndroidGreen),
                        startIndex,
                        endIndex,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString
                } else {
                    food.name
                }
            } else {
                food.name
            }

            nameFood.text = highlightedText

            itemView.setOnClickListener {
                onFoodItemClickListener.onFoodItemClick(food)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(filteredList: MutableList<FoodData>, searchQuery: String) {
        this.filteredList.clear()
        this.filteredList.addAll(filteredList)
        this.searchQuery = searchQuery
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = filteredList[position]
        holder.bind(food)
    }
}