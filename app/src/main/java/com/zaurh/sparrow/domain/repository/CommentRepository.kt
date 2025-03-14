package com.zaurh.sparrow.domain.repository

import com.zaurh.sparrow.data.models.CommentData
import com.zaurh.sparrow.data.models.CommentWithAuthor
import com.zaurh.sparrow.util.Resource
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun addComment(commentData: CommentData): Flow<Resource<CommentData>>
    fun likeComment(commentId: String, userId: String): Flow<Resource<String>>
    fun getComments(postId: String): Flow<Resource<List<CommentWithAuthor>>>
}