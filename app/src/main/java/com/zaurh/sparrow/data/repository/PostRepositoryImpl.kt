package com.zaurh.sparrow.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zaurh.sparrow.data.models.PostData
import com.zaurh.sparrow.data.models.PostWithAuthor
import com.zaurh.sparrow.data.models.UserData
import com.zaurh.sparrow.domain.repository.PostRepository
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
): PostRepository {

    private val postCollection = firestore.collection("posts")

    private fun getUserData(uid: String): Flow<Resource<UserData>> {
        return userRepository.getUserData(uid)
    }

    override fun getFeedPosts(): Flow<Resource<List<PostWithAuthor>>> = callbackFlow {
        trySend(Resource.Loading())

        val listener = postCollection.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Unknown error"))
                    return@addSnapshotListener
                }

                snapshot?.let { querySnapshot ->
                    val posts = querySnapshot.toObjects(PostData::class.java)

                    launch {
                        val postsWithAuthors = posts.mapNotNull { post ->
                            if (post.authorId.isBlank()) return@mapNotNull null

                            val author = getUserData(post.authorId).firstOrNull {
                                it is Resource.Success
                            }?.let {
                                (it as Resource.Success).data
                            }

                            PostWithAuthor(post, author ?: UserData())
                        }
                        trySend(Resource.Success(postsWithAuthors))
                    }
                }
            }

        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)

    override fun getSpecificPost(postId: String): Flow<Resource<PostData>> =
        callbackFlow<Resource<PostData>> {
            trySend(Resource.Loading())

            val listener = postCollection.document(postId).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Unknown error"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val postData = snapshot.toObject(PostData::class.java)
                    if (postData != null) {
                        trySend(Resource.Success(postData))
                    } else {
                        trySend(Resource.Error("User data is null"))
                    }
                } else {
                    trySend(Resource.Error("User data not found"))
                }
            }
            awaitClose { listener.remove() }
        }.flowOn(Dispatchers.IO)

    override fun getUserPosts(
        userId: String
    ): Flow<Resource<List<PostData>>> = callbackFlow<Resource<List<PostData>>> {
        trySend(Resource.Loading())

        val listener = postCollection
            .whereEqualTo("authorId", userId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Unknown error"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val posts = snapshot.toObjects(PostData::class.java)
                    trySend(Resource.Success(posts))
                } else {
                    trySend(Resource.Error("Posts not found"))
                }
            }

        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)

    override fun addPost(postData: PostData): Flow<Resource<PostData>> = flow {
        emit(Resource.Loading())
        try {
            postCollection.document(postData.id).set(postData).await()
            emit(Resource.Success(postData))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun likePost(postId: String, userId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val batch = firestore.batch()
            val postRef = postCollection.document(postId)

            batch.update(postRef, "likes", FieldValue.arrayUnion(userId))

            batch.commit().await()

            emit(Resource.Success(userId))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun unlikePost(postId: String, userId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val batch = firestore.batch()
            val postRef = postCollection.document(postId)

            batch.update(postRef, "likes", FieldValue.arrayRemove(userId))

            batch.commit().await()

            emit(Resource.Success("Unliked successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

}