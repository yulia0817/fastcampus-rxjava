package com.maryang.fastrxjava.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maryang.fastrxjava.entity.GithubRepo

@Dao
interface GithubDao {
    @Query("SELECT * FROM githubrepo")
    fun selectAll(): List<GithubRepo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: GithubRepo)
}
