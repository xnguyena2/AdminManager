package com.example.adminmanager.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.adminmanager.R
import entity.BeerUnitOrder
import order.OrderSearchResult
import java.text.NumberFormat
import java.util.*

class OrderDetailAdapter(val context : Context, val orderUnit: MutableList<BeerUnitOrder>): BaseAdapter() {

    constructor(context : Context, orderData: OrderSearchResult.PackageOrderData): this(context, conver(orderData)) {
    }

    override fun getCount(): Int {
        return orderUnit.size
    }

    override fun getItem(p0: Int): Any {
        return orderUnit[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view =
            p1 ?: LayoutInflater.from(context).inflate(R.layout.list_order_category_item, p2, false)
        val detail: TextView = view.findViewById(R.id.category_detail)
        val item = orderUnit[p0]

        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("VND")
        val price = format.format(item.price)

        detail.text = "${item.number_unit} ${item.name}($price)"

        return view
    }

    companion object{
        fun conver(orderData: OrderSearchResult.PackageOrderData) : MutableList<BeerUnitOrder>{
            val listItem = mutableListOf<BeerUnitOrder>()
            orderData.beerOrderList.forEach { orderData->
                orderData.beerUnitOrderList
                    .map {
                        it.name = "[${orderData.name} ${it.name}]"
                        it
                    }
                    .forEach { unitOrder ->
                    listItem.add(unitOrder)
                }
            }
            return listItem
        }
    }
}