package com.example.trackit.ui.Nutrition

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R

class FoodAdapter(
    foodList: MutableList<FoodData>,
    val context: Context,
    val onFoodItemClickListener: OnFoodItemClickListener) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    interface OnFoodItemClickListener {
        fun onFoodItemClick(food: FoodData)
    }

    private var filteredList: MutableList<FoodData> = foodList.toMutableList()

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

            itemView.setOnClickListener {
                onFoodItemClickListener.onFoodItemClick(food)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(filteredList: MutableList<FoodData>) {
        this.filteredList = filteredList
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