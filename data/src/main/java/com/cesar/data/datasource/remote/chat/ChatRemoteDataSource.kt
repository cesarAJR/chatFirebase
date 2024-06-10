package com.cesar.data.datasource.remote.chat


import android.content.SharedPreferences
import android.util.Log
import com.cesar.data.remote.model.toMessage
import com.cesar.data.remote.model.toMessageList
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.concurrent.Flow

class ChatRemoteDataSource(private val firebaseBD: FirebaseFirestore,private val sharedPreferences: SharedPreferences):IChatRemoteDataSource {
    override suspend fun sendMessage(
        toUserId: String,
        message: String,
        isConnected:Boolean,
        flow: MutableStateFlow<Message>
    ): StateFlow<Message> {
        val messageId = UUID.randomUUID().toString()
        val formatterHour = DateTimeFormatter.ofPattern("hh:mm a")
        val currentHour = LocalDateTime.now().format(formatterHour)

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)

        val from = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id

        val data = hashMapOf(
            "fromUserId" to from,
            "message" to message,
            "toUserId" to toUserId,
            "dateTime" to current,
            "hour" to currentHour,
            "messageId" to messageId
        )

        val messageData = Message(
            message = message,
            fromUserId = from,
            toUserId=toUserId,
            hour = currentHour,
            messageId = messageId,
            dateTime = current
        )
        if (isConnected){
            val docFT =  firebaseBD.collection("messages")
                .document("$from-$toUserId")
                .get()
                .await()
            if (docFT.data?.get("users")!=null){
                data["chatId"] = "$from-$toUserId"
                messageData.chatId = "$from-$toUserId"
                firebaseBD.collection("messages")
                    .document("$from-$toUserId")
                    .collection("messages")
                    .add(data)
                    .addOnSuccessListener {
                        messageData.pendingSync = false
                        flow.value = messageData

                    }.addOnFailureListener {
                        messageData.pendingSync = true
                        flow.value = messageData
                    }
            }else{
                val docTF =  firebaseBD.collection("messages")
                    .document("$toUserId-$from")
                    .get()
                    .await()
                if (docTF.data?.get("users")!=null){
                    data["chatId"] = "$toUserId-$from"
                    messageData.chatId = "$toUserId-$from"
                    firebaseBD.collection("messages")
                        .document("$toUserId-$from")
                        .collection("messages")
                        .add(data)
                        .addOnSuccessListener {
                            messageData.pendingSync = false
                            flow.value = messageData
                        }.addOnFailureListener {
                            messageData.pendingSync = true
                            flow.value = messageData
                        }
                }else{
                    val users = mutableListOf(from!!,toUserId)

                    val members = hashMapOf(
                        "users" to users,
                    )
                    firebaseBD.collection("messages")
                        .document("$from-$toUserId")
                        .set(members)
                        .addOnSuccessListener {
                            data["chatId"] = "$from-$toUserId"
                            messageData.chatId = "$from-$toUserId"
                            firebaseBD.collection("messages")
                                .document("$from-$toUserId")
                                .collection("messages")
                                .add(data)
                                .addOnSuccessListener {
                                    messageData.pendingSync = false
                                    flow.value = messageData
                                }.addOnFailureListener {
                                    messageData.pendingSync = true
                                    flow.value = messageData
                                }
                        }.addOnFailureListener {
                            messageData.pendingSync = true
                            flow.value = messageData
                        }
                }
            }
        }else{
            messageData.pendingSync = true
            flow.value=messageData
        }


        return flow
    }

    override suspend fun getMessage(
        toUserId: String,
        isConnected: Boolean,
        flow: MutableStateFlow<Message>
    ): StateFlow<Message> {
       var i=0
    val from = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id
      val queryToFrom = firebaseBD.collection("messages")
            .document("$toUserId-$from")
            .collection("messages")
            .orderBy("dateTime",Query.Direction.DESCENDING)
            .limit(1)
    val queryFromTo = firebaseBD.collection("messages")
            .document("$from-$toUserId")
            .collection("messages")
            .orderBy("dateTime",Query.Direction.DESCENDING)
            .limit(1)
        queryToFrom.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                snapshot.forEach {
                    val message = it.toMessage()
                    if (i>0){
                        flow.value = message
                    }
                    i++
                }
            } else {
            }
        }
        queryFromTo.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                snapshot.forEach {
                    val message = it.toMessage()
                    if (i>0){
                        flow.value = message
                    }
                    i++
                }
            } else {
            }
        }

        return flow
    }

    override suspend fun getListMessage(toUserId: String): MutableList<Message> {
        val from = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id

        var docRef:Query?=null
        val docFT =  firebaseBD.collection("messages")
            .document("$from-$toUserId")
            .get().await()
        if (docFT.data?.get("users")!=null){
            docRef = firebaseBD.collection("messages")
                .document("$from-$toUserId")
                .collection("messages")
                .orderBy("dateTime",Query.Direction.ASCENDING)

        }else{
            val docTF =  firebaseBD.collection("messages")
                .document("$toUserId-$from")
                .get().await()
            if (docTF.data?.get("users")!=null){
                docRef = firebaseBD.collection("messages")
                    .document("$toUserId-$from")
                    .collection("messages")
                    .orderBy("dateTime",Query.Direction.ASCENDING)

            }
        }

       val result = docRef?.get()?.await()
        return if (result?.documents!=null){
            result.documents!!.toMessageList().toMutableList()
        }else{
            mutableListOf()
        }
    }

    override suspend fun getOnlineByUser(
        userId: String,
        flow: MutableStateFlow<String>
    ): StateFlow<String> {
        val query = firebaseBD.collection("users")
            .document(userId)

        query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
               flow.value = snapshot.data?.get("online").toString()
            } else {
            }
        }
        return flow
    }

    override suspend fun sendPendingMessage(
        message: Message,
        flow: MutableStateFlow<Message>
    ): StateFlow<Message> {

        val data = hashMapOf(
            "fromUserId" to message.fromUserId,
            "message" to message.message,
            "toUserId" to message.toUserId,
            "dateTime" to message.dateTime,
            "hour" to message.hour,
            "messageId" to message.messageId,
        )

        if (message.chatId!=null && message.chatId!!.isNotEmpty()){
            data["chatId"] = message.chatId
            firebaseBD.collection("messages")
                .document(message.chatId!!)
                .collection("messages")
                .add(data)
                .addOnSuccessListener {
                    message.pendingSync = false
                    flow.value = message
                }.addOnFailureListener {
                    message.pendingSync = true
                    flow.value = message
                }
        }else{
            val from = message.fromUserId
            val toUserId = message.toUserId
            return sendAndValidateMessage(from?:"",toUserId?:"",message,data,flow)
        }

        return  flow
    }

   private suspend fun sendAndValidateMessage(from:String, toUserId:String,message: Message,data:HashMap<String,String?>,flow: MutableStateFlow<Message>):StateFlow<Message>{
        val docFT =  firebaseBD.collection("messages")
            .document("$from-$toUserId")
            .get()
            .await()
        if (docFT.data?.get("users")!=null){
            data["chatId"] = "$from-$toUserId"
            message.chatId = "$from-$toUserId"
            firebaseBD.collection("messages")
                .document("$from-$toUserId")
                .collection("messages")
                .add(data)
                .addOnSuccessListener {
                    message.pendingSync = false
                    flow.value = message

                }.addOnFailureListener {
                    message.pendingSync = true
                    flow.value = message
                }
        }else{
            val docTF =  firebaseBD.collection("messages")
                .document("$toUserId-$from")
                .get()
                .await()
            if (docTF.data?.get("users")!=null){
                data["chatId"] = "$toUserId-$from"
                message.chatId = "$toUserId-$from"
                firebaseBD.collection("messages")
                    .document("$toUserId-$from")
                    .collection("messages")
                    .add(data)
                    .addOnSuccessListener {
                        message.pendingSync = false
                        flow.value = message
                    }.addOnFailureListener {
                        message.pendingSync = true
                        flow.value = message
                    }
            }else{
                val users = mutableListOf(from!!,toUserId)

                val members = hashMapOf(
                    "users" to users,
                )
                firebaseBD.collection("messages")
                    .document("$from-$toUserId")
                    .set(members)
                    .addOnSuccessListener {
                        data["chatId"] = "$from-$toUserId"
                        message.chatId = "$from-$toUserId"
                        firebaseBD.collection("messages")
                            .document("$from-$toUserId")
                            .collection("messages")
                            .add(data)
                            .addOnSuccessListener {
                                message.pendingSync = false
                                flow.value = message
                            }.addOnFailureListener {
                                message.pendingSync = true
                                flow.value = message
                            }
                    }.addOnFailureListener {
                        message.pendingSync = true
                        flow.value = message
                    }
            }
        }
       return flow
    }


}