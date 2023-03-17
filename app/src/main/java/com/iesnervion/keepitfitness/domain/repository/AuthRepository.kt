package com.iesnervion.keepitfitness.domain.repository

import com.iesnervion.keepitfitness.util.Resource

interface AuthRepository {

    suspend fun login (email:String, password:String) : Boolean

    suspend fun signup (email:String, password:String) : Boolean

}