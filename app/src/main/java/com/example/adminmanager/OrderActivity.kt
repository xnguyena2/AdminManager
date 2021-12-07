package com.example.adminmanager

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.adminmanager.databinding.ActivityOrderBinding
import com.google.gson.Gson
import order.OrderSearchResult
import java.text.NumberFormat
import java.util.*
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adminmanager.order.OrderDetailAdapter
import entity.PackageOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val context = this

        intent.extras?.getString("order")?.let { orderString ->
            try {
                val orderSearchResult =
                    Gson().fromJson(orderString, OrderSearchResult.PackageOrderData::class.java)
                orderSearchResult?.let {
                    binding.name.text = it.reciver_fullname
                    binding.city.text = it.region
                    binding.district.text = it.district
                    binding.ward.text = it.ward
                    binding.address.text = it.reciver_address


                    val format: NumberFormat = NumberFormat.getCurrencyInstance()
                    format.maximumFractionDigits = 0
                    format.currency = Currency.getInstance("VND")
                    binding.money.text = format.format(it.real_price)


                    binding.phone.text = it.phone_number
                    binding.phone.setOnClickListener { phoneView ->
                        val intentDial =
                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + it.phone_number))
                        this.startActivity(intentDial)
                    }

                    binding.btnSuccess.setOnClickListener {

                        lifecycleScope.launch(Dispatchers.IO) {
                            val result = com.example.adminmanager.network.connection.ChangeOrderStatus(
                                orderSearchResult.package_order_second_id,
                                PackageOrder.Status.DONE
                            )
                            Log.d("Testing data", "response: $result")
                            if (result != null) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()
                                    done(orderSearchResult.package_order_second_id)
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                    binding.btnCancel.setOnClickListener {

                        lifecycleScope.launch(Dispatchers.IO) {
                            val result = com.example.adminmanager.network.connection.ChangeOrderStatus(
                                orderSearchResult.package_order_second_id,
                                PackageOrder.Status.CANCEL
                            )
                            Log.d("Testing data", "response: $result")
                            if (result != null) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()
                                    done(orderSearchResult.package_order_second_id)
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                    binding.listProductCategory.adapter = OrderDetailAdapter(this, orderSearchResult)

                }
            } catch (ex: Exception) {
            }
        }
    }

    fun done(result: String){
        val data = Intent()
        data.putExtra("id", result)
        setResult(RESULT_OK, data)
        finish()
    }
}