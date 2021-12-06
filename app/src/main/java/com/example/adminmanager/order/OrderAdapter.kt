package com.example.adminmanager.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.adminmanager.R
import order.OrderSearchResult

class OrderAdapter(val context: Context, val orders: OrderSearchResult): BaseAdapter() {

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
        price.text = orders!!.result[p0].real_price.toString()
        return view
    }
}