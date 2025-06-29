package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document_embeddings")
data class DocumentEmbeddings(
    @PrimaryKey
    val embeddingId: String,  // UUID

    val documentName: String, // varchar(500), not null

    val documentType: String?, // varchar(50), nullable

    val chunkText: String,     // text, not null

    val chunkIndex: Int,       // integer, not null

    val metadata: String?,     // jsonb as raw JSON string

    val createdAt: String?     // timestamp with time zone, ISO string
)
