package com.akiramenaide.capstoneproject.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akiramenaide.capstoneproject.R
import com.akiramenaide.capstoneproject.databinding.FragmentHomeBinding
import com.akiramenaide.capstoneproject.ui.util.MyColors
import com.akiramenaide.capstoneproject.ui.util.PredictedObject
import com.akiramenaide.core.data.source.remote.network.ApiResponse
import com.akiramenaide.core.domain.model.Fruit
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    private val brightColors = MyColors.brightColorArray
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var barChart: BarChart
    private lateinit var fruitList: List<Fruit>
    private var insertedFruit: Fruit? = null
    private var fruitInfo: PredictedObject? = null
    private var myBitmap: Bitmap? = null
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var resizedBitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initial cloud database
//        val  database = FirebaseDatabase.getInstance().reference
//        hide textview utk melihat hasil db cloud
        fragmentHomeBinding.homepage.tvCoba.visibility = View.GONE
        //=====================================
        barChart = fragmentHomeBinding.homepage.barChart
        barChart.visibility = View.GONE
        homeViewModel.getAllFruits().observe(viewLifecycleOwner, {
            fruitList = it
            drawBarChart(fruitList)
        })
        fragmentHomeBinding.homepage.btnPickImg.setOnClickListener {
            fragmentHomeBinding.viewEmpty.root.visibility = View.GONE
            if (requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                getImage()
            }
        }

        fragmentHomeBinding.homepage.btnPredictImg.setOnClickListener {
            when {
                myBitmap != null && fruitInfo != null -> {
                    Toast.makeText(activity, "Image was predicted", Toast.LENGTH_SHORT).show()
                }
                myBitmap == null -> {
                    Toast.makeText(activity, "Please insert an Image", Toast.LENGTH_SHORT).show()
                }
                myBitmap != null && fruitInfo == null -> {
                    predictImage()
                    fragmentHomeBinding.predictImg.root.visibility = View.GONE
                }
            }
        }

        fragmentHomeBinding.homepage.btnInsertDb.setOnClickListener {

            val fruit = fruitInfo
            var isInDb = false

            fruit?.let { myFruit ->
                if (myFruit.className != "Uncategorized") {
                    var fruitName = myFruit.className
                    var isFresh = false

                    if (fruitName.contains("Fresh")) {
                        fruitName = fruitName.removePrefix("Fresh").trim()
                        isFresh = true
                    } else {
                        fruitName = fruitName.removePrefix("Rotten").trim()
                    }

                    for (element in fruitList) {
                        if (fruitName.lowercase() == element.name.lowercase()) {
                            element.total += 1
                            if (isFresh) {
                                element.freshTotal += 1
                            }
                            isInDb = true
                            homeViewModel.updateFruitInfo(element)
//                            kirim ke database cloud
//                            database.child(fruitName.trim())
//                                .setValue(Fruit("${element.total}","${element.freshTotal}"))
                        }
                    }

                    if (!isInDb) {
                        insertedFruit =
                            Fruit(fruitList.size + 1, fruitName, 1, if (isFresh) 1 else 0)
                        insertedFruit?.let {
                            homeViewModel.insertFruit(it)
                        }
//                        kirim ke database cloud
//                        database.child(fruitName.trim())
//                            .setValue(Fruit("1","${if (isFresh) 1 else 0}"))
                    }
                } else {
                    Toast.makeText(activity, "Data not valid!", Toast.LENGTH_SHORT).show()
                }
            }
        }

//        Get Data from cloud database
//        //GetData database cloud
//        val getData = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val sb = StringBuilder()
//                for (i in snapshot.children) {
//                    val jumlah = i.child("total").value
//                    sb.append("Classification:\t${i.key}\n${i.key} Quantity:\t$jumlah\n\n")
//                }
//                fragmentHomeBinding.homepage.tvCoba.text = sb
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        }
//        database.addValueEventListener(getData)
//        database.addListenerForSingleValueEvent(getData)

    }

    private fun drawBarChart(fruits: List<Fruit>) {
        if (fruits.isNotEmpty()) {
            barChart.visibility = View.VISIBLE
            val barValues = ArrayList<BarEntry>()
            val fruitNames = ArrayList<String>()
            var max = 0f
            var x = 0f
            for (element in fruits) {
                barValues.add(
                    BarEntry(
                        x,
                        element.total.toFloat()
                    )
                )
                x++
                fruitNames.add(element.name)
                if (element.total.toFloat() > max) {
                    max = element.total.toFloat()
                }
            }
            val dataSet = BarDataSet(barValues, "")

            barChart.apply {
                data = BarData(dataSet).apply {
                    setValueTextSize(10f)
                    setValueTextColor(Color.WHITE)
                }
                description = null
                setFitBars(true)
                setScaleEnabled(false)
                setPinchZoom(false)
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(fruitNames)
                    labelCount = fruitNames.size
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = Color.WHITE
                }
                axisLeft.apply {
                    axisMinimum = 0f
                    textColor = Color.WHITE
                }
                axisRight.apply {
                    axisMinimum = 0f
                    textColor = Color.WHITE
                }
                legend.isEnabled = false
                animateY(1000)
                invalidate()
            }
            dataSet.colors = brightColors
        }

    }

    private fun getImage() {
        fragmentHomeBinding.predictImg.root.visibility = View.GONE
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun predictImage() {
        myBitmap?.let { originalBitmap ->
            val resizedImg = Bitmap.createScaledBitmap(originalBitmap, 224, 224, true)
            resizedBitmap = resizedImg
            val byteArrayOutputStream = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            val imgString = Base64.encode(imageBytes, Base64.DEFAULT)
            val utfString = String(imgString, charset("UTF-8"))
            val jsonString = "{\"data\": \"$utfString\"}"
            postString(jsonString)
            fragmentHomeBinding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun postString(buffer: String) {
        val response = homeViewModel.getPredict(buffer)
        response.observe(viewLifecycleOwner, {
            if (it != null) {
                Log.d("NGEPOT", "Tidak Null Boss")
                when (it) {
                    is ApiResponse.Empty -> {
                        fruitInfo = null
                        fragmentHomeBinding.apply {
                            progressBar.visibility - View.GONE
                            viewEmpty.root.visibility = View.VISIBLE
                            predictImg.root.visibility = View.GONE
                            viewEmpty.tvEmpty.text = getString(R.string.no_data)
                        }
                    }
                    is ApiResponse.Success -> {
                        Log.d("Success", it.data.prediction.toString())
                        fragmentHomeBinding.apply {
                            predictImg.predictionNumTxt.visibility = View.VISIBLE
                            predictImg.predictionTxt.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            predictImg.root.visibility = View.VISIBLE
                            viewEmpty.root.visibility = View.GONE
                            predictImg.myImg.setImageBitmap(resizedBitmap)
                            if (it.data.className.trim() == "Uncategorized") {
                                fruitInfo = PredictedObject("Uncategorized", 0f)
                                predictImg.predictionNumTxt.visibility = View.GONE
                                predictImg.predictionTxt.text = getString(R.string.not_iddentified)
                            } else {
                                predictImg.predictionTxt.text =
                                    getString(R.string.prediction, it.data.className)
                                predictImg.predictionNumTxt.text = getString(
                                    R.string.percentage,
                                    "${it.data.percentage.toString().trim()}%"
                                )
                                fruitInfo = PredictedObject(it.data.className, it.data.percentage)
                            }
                        }
                    }
                    is ApiResponse.Error -> {
                        fruitInfo = null
                        fragmentHomeBinding.apply {
                            progressBar.visibility = View.GONE
                            viewEmpty.root.visibility = View.VISIBLE
                            viewEmpty.tvEmpty.text = it.errorMessage
                        }
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImage()
                } else {
                    Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val img = data?.data
            @Suppress("DEPRECATION")
            myBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, img)
            fragmentHomeBinding.predictImg.apply {
                root.visibility = View.VISIBLE
                predictionNumTxt.visibility = View.GONE
                predictionTxt.visibility = View.GONE
            }

            fragmentHomeBinding.predictImg.myImg.apply {
                setImageBitmap(myBitmap)
            }
            fruitInfo = null
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
        const val PERMISSION_CODE = 1001
    }
}

class Fruit(var total: String, var freshTotal:String)