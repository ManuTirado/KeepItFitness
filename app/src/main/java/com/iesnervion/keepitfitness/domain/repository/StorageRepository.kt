package com.iesnervion.keepitfitness.domain.repository

import android.net.Uri
import java.net.URI

interface StorageRepository {

    suspend fun uploadImage(uri: Uri): Boolean

    suspend fun getPhotoURL(): Uri?
}