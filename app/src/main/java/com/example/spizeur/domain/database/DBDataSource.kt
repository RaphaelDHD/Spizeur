package com.example.spizeur.domain.database

import com.example.spizeur.models.Order
import com.example.spizeur.models.Product
import com.example.spizeur.models.User

object DBDataSource {

    suspend fun insertUser(user: User) {
        SpizeurDataBase.getInstance().userDAO().insertUser(user)
    }

    suspend fun updateUser(user: User) {
        SpizeurDataBase.getInstance().userDAO().updateUser(user)
    }


    suspend fun getUser(email: String): User {
        return SpizeurDataBase.getInstance().userDAO().getUser(email)
    }

    suspend fun addProducts(products: List<Product>) {
        SpizeurDataBase.getInstance().productDAO().insertProducts(products)
    }

    suspend fun getAllProducts(): List<Product> {
        return SpizeurDataBase.getInstance().productDAO().getAllProduct()
    }

    suspend fun deleteAllProduct() {
        SpizeurDataBase.getInstance().productDAO().deleteAllProducts()
    }

    suspend fun setUserNewUsername(username: String, userId: Int)
    {
        SpizeurDataBase.getInstance().userDAO().setUserNewUsername(username, userId)
    }

    suspend fun setUserNewEmail(email: String, userId: Int)
    {
        SpizeurDataBase.getInstance().userDAO().setUserNewEmail(email, userId)
    }

    suspend fun setUserNewPassword(password: String, userId: Int)
    {
        SpizeurDataBase.getInstance().userDAO().setUserNewPassword(password, userId)
    }
    suspend fun getProductListByIds(listId: List<Int>): MutableList<Product> {
        val productList = mutableListOf<Product>()
        for (id in listId) {
            productList.add(SpizeurDataBase.getInstance().productDAO().getProductById(id))
        }
        return productList
    }

    suspend fun insertOrder(value: Order) {
        SpizeurDataBase.getInstance().orderDAO().insertOrder(value)
    }

    suspend fun updateOrder(value: Order) {
        SpizeurDataBase.getInstance().orderDAO().updateOrder(value)
    }

    suspend fun getOrderByUserId(userId: Int): Order {
        return SpizeurDataBase.getInstance().orderDAO().getOrderByUserId(userId)
    }

}