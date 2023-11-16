package com.example.spizeur.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.spizeur.R
import com.example.spizeur.domain.ProductsRepository
import com.example.spizeur.models.Product
import com.example.spizeur.ui.home.HomeViewModel
import com.example.spizeur.ui.productInfo.ProductInfoActivity

class ProductAdapter (private var productData: List<Product>):
    RecyclerView.Adapter<ProductAdapter.DataViewHolder>()
{
    private var productsList: List<Product> = ArrayList()

    init {
        this.productsList = productData
    }

    var onItemClick: ((Product) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(productsList[adapterPosition])
                val repo = ProductsRepository
                repo.setSelectedProduct(productsList[adapterPosition])
                val intent = Intent(itemView.context, ProductInfoActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(result : Product) {
            itemView.findViewById<TextView>(R.id.name).text = result.title

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_product, parent, false
        )
    )
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size


}