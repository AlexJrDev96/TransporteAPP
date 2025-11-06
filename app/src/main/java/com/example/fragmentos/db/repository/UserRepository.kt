package com.example.fragmentos.db.repository

import com.example.fragmentos.db.dao.UserDao
import com.example.fragmentos.db.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun findByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }
}