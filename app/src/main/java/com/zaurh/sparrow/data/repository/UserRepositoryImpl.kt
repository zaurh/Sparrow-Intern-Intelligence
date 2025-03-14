package com.zaurh.sparrow.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.zaurh.sparrow.data.AppwriteManager
import com.zaurh.sparrow.data.models.ChatData
import com.zaurh.sparrow.data.models.ChatDataWithAuthor
import com.zaurh.sparrow.data.models.MessageData
import com.zaurh.sparrow.data.models.UserData
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.util.Resource
import io.appwrite.ID.Companion.unique
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.InputFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val userCollection = firestore.collection("users")
    private val commentCollection = firestore.collection("comments")
    private val postCollection = firestore.collection("posts")

    override fun getFollowers(userId: String): Flow<Resource<List<UserData>>> = flow {
        emit(Resource.Loading())
        try {
            val userData =
                userCollection.document(userId).get().await().toObject(UserData::class.java)

            val followerIds = userData?.followers ?: emptyList()

            if (followerIds.isEmpty()) {
                emit(Resource.Success(emptyList()))
                return@flow
            }

            val chunkedIds = followerIds.chunked(10)

            val userList = coroutineScope {
                chunkedIds.map { chunk ->
                    async {
                        userCollection.whereIn("userId", chunk)
                            .get()
                            .await()
                            .toObjects(UserData::class.java)
                    }
                }.awaitAll().flatten()
            }

            emit(Resource.Success(userList))

        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

    override fun getFollowing(userId: String): Flow<Resource<List<UserData>>> = flow {
        emit(Resource.Loading())
        try {
            val userData =
                userCollection.document(userId).get().await().toObject(UserData::class.java)

            val followingIds = userData?.following ?: emptyList()

            if (followingIds.isEmpty()) {
                emit(Resource.Success(emptyList()))
                return@flow
            }

            val chunkedIds = followingIds.chunked(10)

            val userList = coroutineScope {
                chunkedIds.map { chunk ->
                    async {
                        userCollection.whereIn("userId", chunk)
                            .get()
                            .await()
                            .toObjects(UserData::class.java)
                    }
                }.awaitAll().flatten()
            }

            emit(Resource.Success(userList))

        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

    override fun followUser(
        userId: String,
        targetUserId: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val batch = firestore.batch()

            val currentUserRef = userCollection.document(userId)
            val targetUserRef = userCollection.document(targetUserId)

            batch.update(currentUserRef, "following", FieldValue.arrayUnion(targetUserId))
            batch.update(targetUserRef, "followers", FieldValue.arrayUnion(userId))

            batch.commit().await()

            emit(Resource.Success("Unfollowed successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun unfollowUser(
        userId: String,
        targetUserId: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val batch = firestore.batch()

            val currentUserRef = userCollection.document(userId)
            val targetUserRef = userCollection.document(targetUserId)

            batch.update(currentUserRef, "following", FieldValue.arrayRemove(targetUserId))
            batch.update(targetUserRef, "followers", FieldValue.arrayRemove(userId))

            batch.commit().await()

            emit(Resource.Success("Unfollowed successfully"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserData(uid: String): Flow<Resource<UserData>> =
        callbackFlow<Resource<UserData>> {
            trySend(Resource.Loading())

            val listener = userCollection.document(uid).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Unknown error"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val userData = snapshot.toObject(UserData::class.java)
                    if (userData != null) {
                        trySend(Resource.Success(userData)) // Emit new updates
                    } else {
                        trySend(Resource.Error("User data is null"))
                    }
                } else {
                    trySend(Resource.Error("User data not found"))
                }
            }
            awaitClose { listener.remove() }
        }.flowOn(Dispatchers.IO)

    override fun updateUserData(userData: UserData): Flow<Resource<UserData>> = flow {
        emit(Resource.Loading())
        try {
            userCollection.document(userData.userId)
                .set(userData, SetOptions.merge())
                .await()
            updateAuthorData(
                collection = commentCollection,
                userId = userData.userId,
                userImage = userData.imageUrl,
                username = userData.username
            )
            updateAuthorData(
                collection = postCollection,
                userId = userData.userId,
                userImage = userData.imageUrl,
                username = userData.username
            )
            emit(Resource.Success(userData))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun updateAuthorData(
        collection: CollectionReference,
        userId: String,
        userImage: String,
        username: String
    ) {
        try {
            val reviewsSnapshot = collection.whereEqualTo("authorId", userId).get().await()
            val batch = firestore.batch()

            for (document in reviewsSnapshot.documents) {
                batch.update(document.reference, "author.username", username)
                batch.update(document.reference, "author.image", userImage)
            }

            batch.commit().await()

        } catch (e: Exception) {
            Log.e("Firestore", "Error updating reviews: ${e.localizedMessage}")
        }
    }


    override fun uploadImage(file: File, context: Context): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        try {
            val appwriteManager = AppwriteManager(context = context)
            val buckedId = "67c9779500314d6619bf"
            val projectId = "67c9770d0010fbe74ecd"

            val response = appwriteManager.storage.createFile(
                bucketId = buckedId,
                fileId = unique(),
                file = InputFile.fromFile(file)
            )

            val fileId = response.id

            val imageUrl =
                "https://cloud.appwrite.io/v1/storage/buckets/$buckedId/files/$fileId/preview?project=$projectId"
            emit(Resource.Success(imageUrl))
        } catch (e: AppwriteException) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override fun searchUsers(username: String): Flow<Resource<List<UserData>>> = flow {
        emit(Resource.Loading())
        try {
            val querySnapshot = userCollection
                .whereGreaterThanOrEqualTo("username", username)
                .whereLessThanOrEqualTo("username", username + "\uf8ff")
                .get()
                .await()

            val userList = querySnapshot.documents.mapNotNull { document ->
                document.toObject(UserData::class.java)
            }

            emit(Resource.Success(userList))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }



}