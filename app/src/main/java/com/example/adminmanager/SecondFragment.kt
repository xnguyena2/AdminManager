package com.example.adminmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import com.example.adminmanager.databinding.FragmentSecondBinding
import com.example.adminmanager.network.login.Token
import com.example.adminmanager.order.OrderAdapter
import com.google.gson.Gson
import entity.PackageOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import order.OrderSearchResult

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {


    init { // Notice that we can safely launch in the constructor of the Fragment.
        lifecycleScope.launch {
            whenStarted {
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = com.example.adminmanager.network.connection.GetAllOrder(PackageOrder.Status.ORDER)
                    val orders = Gson().fromJson(result, OrderSearchResult::class.java)
                    Log.d("Testing data", "response: ${orders.result}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Success loading data!", Toast.LENGTH_LONG).show()
                        //findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                        binding.listOrder.adapter = context?.let {
                            adapter = OrderAdapter(it, orders, intentLauncher)
                            adapter
                        }
                    }
                }
            }
        }
    }

    var adapter: OrderAdapter? = null
    val intentLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val id = result.data?.getStringExtra("id")
                Toast.makeText(context, "id: $id", Toast.LENGTH_LONG).show()
                if (id != null) {
                    adapter?.update(id)
                }
            }
        }

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}