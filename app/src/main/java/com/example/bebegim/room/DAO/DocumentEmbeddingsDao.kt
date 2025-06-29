package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.DocumentEmbeddings

@Dao
interface DocumentEmbeddingsDao : BaseDao<DocumentEmbeddings> {
    @Query("SELECT * FROM document_embeddings WHERE embeddingId = :embeddingId ORDER BY embeddingId DESC")
    suspend fun getDocumentEmbeddingsById(embeddingId: String): DocumentEmbeddings?
}