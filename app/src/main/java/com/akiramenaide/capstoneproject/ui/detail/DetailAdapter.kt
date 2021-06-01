package com.akiramenaide.capstoneproject.ui.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akiramenaide.capstoneproject.R
import com.akiramenaide.core.domain.model.Fruit
import com.akiramenaide.capstoneproject.databinding.ListDetailBinding
import com.akiramenaide.capstoneproject.ui.util.MyColors
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class DetailAdapter: RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private val fruitList = ArrayList<Fruit>()

    inner class DetailViewHolder(private val binding: ListDetailBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(fruit: Fruit){
            with(binding) {
                judul.text = itemView.context.getString(R.string.in_dataset, fruit.name)
                skor.text = fruit.total.toString()
                persen.text = itemView.context.getString(R.string.in_good_quality, fruit.freshPercentage.toString(), fruit.name)
                Log.d("Element", fruit.name)
                bindBarChart(fruit)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = ListDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) = holder.bind(fruitList[position])

    override fun getItemCount(): Int = fruitList.size

    fun setFruitsData(fruits: List<Fruit>?){
        if (fruits == null) return
        fruitList.clear()
        fruitList.addAll(fruits)
        Log.d("RecyclerView", fruits.size.toString())
    }

    private fun ListDetailBinding.bindBarChart(fruit: Fruit){
        val freshTotal = fruit.freshTotal.toFloat()
        val notFreshTotal = fruit.notFreshTotal.toFloat()
        val barValues = ArrayList<BarEntry>()
        barValues.add(BarEntry(0f, floatArrayOf(freshTotal, notFreshTotal)))
        val dataSet = BarDataSet(barValues, "")
        dataSet.colors = MyColors.stackedColorArray
        dataSet.setValueTextColors(MyColors.stackedColorArray)
        dataSet.valueTextSize = 10f
        dataSet.stackLabels = arrayOf("Fresh Fruit", "Bad Fruit")

        horizontalBarChart.apply {
            data = BarData(dataSet)
            description = null
            setTouchEnabled(false)
            setScaleEnabled(false)
            setPinchZoom(false)
            xAxis.isEnabled = false
            axisRight.isEnabled = false
            animateY(1000)
            invalidate()
        }
    }
}