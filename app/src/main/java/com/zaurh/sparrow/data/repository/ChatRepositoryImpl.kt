package com.zaurh.sparrow.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zaurh.sparrow.data.models.ChatData
import com.zaurh.sparrow.data.models.ChatDataWithAuthor
import com.zaurh.sparrow.data.models.MessageData
import com.zaurh.sparrow.data.models.UserData
import com.zaurh.sparrow.domain.repository.ChatRepository
import com.zaurh.sparrow.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
): ChatRepository {

    private val userCollection = firestore.collection("users")
    private val messageCollection = firestore.collection("messages")

    override fun getMessages(
        senderId: String,
        receiverId: String
    ): Flow<Resource<List<MessageData>>> = callbackFlow {

        trySend(Resource.Loading())

        val listener = messageCollection
            .where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("senderId", senderId),
                        Filter.equalTo("receiverId", receiverId)
                    ),
                    Filter.and(
                        Filter.equalTo("senderId", receiverId),
                        Filter.equalTo("receiverId", senderId)
                    )
                )
            )
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Unknown Firestore error"))
                    return@addSnapshotListener
                }
                snapshot?.let { querySnapshot ->
                    val messages = querySnapshot.toObjects(MessageData::class.java)
                    trySend(Resource.Success(messages))
                }
            }

        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)

    override fun sendMessage(messageData: MessageData): Flow<Resource<MessageData>> = flow {
        emit(Resource.Loading())
        try {
            val senderSnapshot = userCollection.document(messageData.senderId).get().await()
            val senderData = senderSnapshot.toObject(UserData::class.java) ?: UserData()
            val senderChatList = senderData.chatList.toMutableList()

            if (senderChatList.any { it.recipientId == messageData.receiverId }) {
                senderChatList.replaceAll { chat ->
                    if (chat.recipientId == messageData.receiverId)
                        chat.copy(
                            lastMessage = messageData.message,
                            timestamp = Timestamp.now()
                        )
                    else
                        chat
                }
            } else {
                senderChatList.add(ChatData(recipientId = messageData.receiverId, lastMessage = messageData.message))
            }

            userCollection.document(messageData.senderId).update("chatList", senderChatList).await()

            val receiverSnapshot = userCollection.document(messageData.receiverId).get().await()
            val receiverData = receiverSnapshot.toObject(UserData::class.java) ?: UserData()
            val receiverChatList = receiverData.chatList.toMutableList()

            if (receiverChatList.any { it.recipientId == messageData.senderId }) {
                receiverChatList.replaceAll { chat ->
                    if (chat.recipientId == messageData.senderId)
                        chat.copy(timestamp = Timestamp.now(), lastMessage = messageData.message)
                    else
                        chat
                }
            } else {
                receiverChatList.add(ChatData(recipientId = messageData.senderId, lastMessage = messageData.message))
            }

            userCollection.document(messageData.receiverId).update("chatList", receiverChatList).await()

            messageCollection.document(messageData.messageId).set(messageData).await()

            emit(Resource.Success(messageData))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

    override fun getChatList(
        currentUserId: String
    ): Flow<Resource<List<ChatDataWithAuthor>>> = callbackFlow<Resource<List<ChatDataWithAuthor>>> {
        trySend(Resource.Loading())

        val listener = userCollection.document(currentUserId)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Unknown error"))
                    return@addSnapshotListener
                }

                documentSnapshot?.let {
                    val userData = it.toObject(UserData::class.java)
                    val chatList = userData?.chatList ?: emptyList()

                    val chatDataWithUserList = mutableListOf<ChatDataWithAuthor>()

                    chatList.forEach { chat ->
                        userCollection.document(chat.recipientId).get()
                            .addOnSuccessListener { recipientDocument ->
                                val recipientData = recipientDocument.toObject(UserData::class.java)

                                recipientData?.let { recipient ->
                                    val chatDataWithUser = ChatDataWithAuthor(
                                        userData = recipient,
                                        chatData = chat
                                    )
                                    chatDataWithUserList.add(chatDataWithUser)

                                    if (chatDataWithUserList.size == chatList.size) {
                                        trySend(Resource.Success(chatDataWithUserList))
                                    }
                                }
                            }
                            .addOnFailureListener {
                                trySend(Resource.Error(it.localizedMessage ?: "Failed to fetch recipient data"))
                            }
                    }
                }
            }

        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)

}