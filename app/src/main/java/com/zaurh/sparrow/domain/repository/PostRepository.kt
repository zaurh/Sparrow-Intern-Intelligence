package com.zaurh.sparrow.domain.repository

import com.zaurh.sparrow.data.models.PostData
import com.zaurh.sparrow.data.models.PostWithAuthor
import com.zaurh.sparrow.util.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getFeedPosts(): Flow<Resource<List<PostWithAuthor>>>
    fun getUserPosts(userId: String): Flow<Resource<List<PostData>>>
    fun getSpecificPost(postId: String): Flow<Resource<PostData>>
    fun addPost(postData: PostData): Flow<Resource<PostData>>
    fun likePost(postId: String, userId: String): Flow<Resource<String>>
    fun unlikePost(postId: String, userId: String): Flow<Resource<String>>
}