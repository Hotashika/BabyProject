package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.DocumentEmbeddings
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentEmbeddingsDao : BaseDao<DocumentEmbeddings> {
    @Query("SELECT * FROM document_embeddings WHERE embeddingId = :embeddingId ORDER BY embeddingId DESC")
    fun getDocumentEmbeddingsById(embeddingId: String): Flow<DocumentEmbeddings?>
}