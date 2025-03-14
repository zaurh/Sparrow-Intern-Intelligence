package com.zaurh.sparrow.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.zaurh.sparrow.data.models.CommentData
import com.zaurh.sparrow.data.models.CommentWithAuthor
import com.zaurh.sparrow.data.models.UserData
import com.zaurh.sparrow.domain.repository.CommentRepository
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

class CommentRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
): CommentRepository {

    private val commentCollection = firestore.collection("comments")

    private fun getUserData(uid: String): Flow<Resource<UserData>> {
        return userRepository.getUserData(uid)
    }
    override fun addComment(commentData: CommentData): Flow<Resource<CommentData>> = flow {
        emit(Resource.Loading())
        try {
            commentCollection.document(commentData.id).set(commentData).await()
            emit(Resource.Success(commentData))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }


    override fun likeComment(commentId: String, userId: String): Flow<Resource<String>> = flow {
        return@flow
    }

    override fun getComments(postId: String): Flow<Resource<List<CommentWithAuthor>>> =
        callbackFlow<Resource<List<CommentWithAuthor>>> {
            trySend(Resource.Loading())

            val listener = commentCollection
                .whereEqualTo("postId", postId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Resource.Error(error.localizedMessage ?: "Unknown error"))
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val comments = snapshot.toObjects(CommentData::class.java)

                        if (comments.isEmpty()) {
                            trySend(Resource.Success(emptyList()))
                            return@addSnapshotListener
                        }

                        launch(Dispatchers.IO) {
                            val commentsWithAuthors = comments.mapNotNull { comment ->
                                if (comment.authorId.isBlank()) return@mapNotNull null

                                val authorFlow = getUserData(comment.authorId)
                                val author =
                                    authorFlow.firstOrNull { it is Resource.Success }?.let {
                                        (it as Resource.Success).data
                                    }

                                CommentWithAuthor(comment, author ?: UserData())
                            }

                            trySend(Resource.Success(commentsWithAuthors))
                        }
                    } else {
                        trySend(Resource.Error("Comments not found"))
                    }
                }

            awaitClose { listener.remove() }
        }.flowOn(Dispatchers.IO)

}