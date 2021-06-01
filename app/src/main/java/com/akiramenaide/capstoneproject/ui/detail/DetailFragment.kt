package com.akiramenaide.capstoneproject.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akiramenaide.capstoneproject.databinding.FragmentDetailBinding
import com.akiramenaide.capstoneproject.ui.util.MyColors
import com.akiramenaide.capstoneproject.ui.util.MyColors.darkCyan
import com.akiramenaide.core.domain.model.Fruit
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DetailFragment : Fragment() {
    private val brightColors = MyColors.brightColorArray
    private lateinit var binding: FragmentDetailBinding
    private lateinit var pieChart: PieChart
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            pieChart = binding.pieChart
            detailViewModel.fruitList.observe(viewLifecycleOwner, {
                drawPieChart(it)
                drawRecyclerView(it)
            })
        }
    }

    private fun drawPieChart(fruits: List<Fruit>){
        val pieValues = ArrayList<PieEntry>()
        for (element in fruits) {
            Log.d("FruitName", element.name)
            pieValues.add(PieEntry(element.total.toFloat(), element.name))
        }
        val dataSet = PieDataSet(pieValues, "")

        pieChart.apply {
            data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter())
                setValueTextSize(12f)
            }
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(10f)
            setHoleColor(darkCyan)
            isRotationEnabled = false
            setUsePercentValues(true)
            animateY(1000)
            description.text = ""
            legend.isEnabled = false
            invalidate()
        }
        dataSet.colors = brightColors

    }

    private fun drawRecyclerView(fruits: List<Fruit>){
        val fruitAdapter = DetailAdapter()
        fruitAdapter.setFruitsData(fruits)
        fruitAdapter.notifyDataSetChanged()
        Log.d("RecyclerView", fruits.size.toString())

        with(binding.rvFruits) {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
            adapter = fruitAdapter
            setHasFixedSize(true)
        }
    }

}