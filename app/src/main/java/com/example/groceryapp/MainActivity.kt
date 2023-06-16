package com.example.groceryapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.groceryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GroceryRVAdapter.GroceryItemClickInterface{

    private lateinit var binding: ActivityMainBinding
    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryViewModel: GroceryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        list = ArrayList<GroceryItems>()

        groceryRVAdapter = GroceryRVAdapter(list,this)
        binding.idRVItems.layoutManager = LinearLayoutManager(this)
        binding.idRVItems.adapter = groceryRVAdapter

        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        groceryViewModel = ViewModelProvider(this,factory).get(GroceryViewModel::class.java)
        groceryViewModel.getAllGroceryItems().observe(this, Observer {
            groceryRVAdapter.list = it
            groceryRVAdapter.notifyDataSetChanged()

        })

        binding.idFABAdd.setOnClickListener {
            openDialog()
        }
    }

     fun openDialog() {
         val dialog = Dialog(this)
         dialog.setContentView(R.layout.grocery_add_dialog)
         val cancelBtn = dialog.findViewById<Button>(R.id.idBtnCancel)
         val addBtn = dialog.findViewById<Button>(R.id.idBtnAdd)
         val itemEdt = dialog.findViewById<EditText>(R.id.idEdtItem)
         val itemQuantityEdt = dialog.findViewById<EditText>(R.id.idEdtQuantity)
         val itemPriceEdt = dialog.findViewById<EditText>(R.id.idEdtPrice)

         cancelBtn.setOnClickListener {
             dialog.dismiss()
         }
         addBtn.setOnClickListener {
             val itemName:String = itemEdt.text.toString()
             val itemQuantity:String = itemQuantityEdt.text.toString()
             val itemPrice:String = itemPriceEdt.text.toString()
             val qty : Int  = itemQuantity.toInt()
             val pr : Int = itemPrice.toInt()
             if(itemName.isNotEmpty()&&itemPrice.isNotEmpty()&&itemQuantity.isNotEmpty()){
                 val items = GroceryItems(itemName,qty,pr)
                 groceryViewModel.insert(items)
                 Toast.makeText(applicationContext,
                     "Item inserted...",
                     Toast.LENGTH_SHORT).show()
                 groceryRVAdapter.notifyDataSetChanged()
                 dialog.dismiss()
             }else{
                 Toast.makeText(applicationContext,
                     "please enter all the data...",
                     Toast.LENGTH_SHORT).show()
             }
         }
         dialog.show()
     }

    override fun onItemClick(groceryItems: GroceryItems){
        groceryViewModel.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext,"Item deleted...",Toast.LENGTH_SHORT).show()
    }
}