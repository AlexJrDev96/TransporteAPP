package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fragmentos.db.entity.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?
}