package com.demo.netflix.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.demo.netflix.R
import com.demo.netflix.modelclasses.CategoryData
class CategorySpinnerAdapter(private var categoryList:ArrayList<CategoryData>,private val context: Context):BaseAdapter() {
    var inflator:LayoutInflater = LayoutInflater.from(context)
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return categoryList.size
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflator.inflate(R.layout.common_spinnerlayout,parent,false)
        val spinnertext:TextView = view!!.findViewById(R.id.spinnertextdisplay)
        spinnertext.text= categoryList[position].catName.toString()
        return view
    }
}