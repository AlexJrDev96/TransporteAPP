package com.example.fragmentos.db.repository

import com.example.fragmentos.db.dao.UserDao
import com.example.fragmentos.db.entity.User

class UserRepository(private val userDao: UserDao) {

    // Função que o LoginViewModel precisa
    suspend fun getUserByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}