package com.example.adminmanager.order

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import com.example.adminmanager.MainActivity
import com.example.adminmanager.OrderActivity
import com.example.adminmanager.R
import com.google.gson.Gson
import order.OrderSearchResult
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class OrderAdapter(val context: Context, val orders: OrderSearchResult, val intentLauncher: ActivityResultLauncher<Intent>): BaseAdapter() {

    override fun getCount(): Int = orders?.count?.toInt() ?: 0

    override fun getItem(p0: Int): OrderSearchResult.PackageOrderData? {
        return orders?.result?.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view =
            p1 ?: LayoutInflater.from(context).inflate(R.layout.list_order_item, p2, false)
        val userName: TextView = view.findViewById(R.id.user_name)
        val price: TextView = view.findViewById(R.id.total_money)

        userName.text = orders!!.result[p0].reciver_fullname
        val money = orders!!.result[p0].real_price


        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("VND")
        price.text = format.format(money)

        view.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("order", Gson().toJson(orders!!.result[p0]))
            intentLauncher.launch(intent)
        }
        return view
    }

    fun update(id: String) {
        val removedItem = orders.result.find {
            it.package_order_second_id == id
        }
        removedItem?.let {
            orders.result.remove(it)
            orders.count--
            notifyDataSetChanged()
        }
    }
}