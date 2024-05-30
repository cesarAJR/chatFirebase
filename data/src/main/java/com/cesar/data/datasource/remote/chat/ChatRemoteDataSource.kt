package com.example.data.datasource.remote.login

import android.content.SharedPreferences
import android.util.Log
import com.cesar.data.datasource.remote.chat.IChatRemoteDataSource
import com.cesar.data.remote.model.toMessageList
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.example.domain.core.Result
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.math.log

class ChatRemoteDataSource(private val firebaseBD: FirebaseFirestore,private val sharedPreferences: SharedPreferences):IChatRemoteDataSource {
    override suspend fun sendMessage(
        fromUserId: String,
        toUserId: String,
        message: String
    ): String {
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
            "messageId" to UUID.randomUUID().toString()
        )

       val docFT =  firebaseBD.collection("messages")
           .document("$from-$toUserId")
            .get()
           .await()
        if (docFT.data?.get("users")!=null){
            firebaseBD.collection("messages")
                .document("$from-$toUserId")
                .collection("messages")
                .add(data)
        }else{
            val docTF =  firebaseBD.collection("messages")
                .document("$toUserId-$from")
                .get()
                .await()
            if (docTF.data?.get("users")!=null){
            firebaseBD.collection("messages")
                    .document("$toUserId-$from")
                    .collection("messages")
                    .add(data)
            }else{
                val users = mutableListOf(from!!,toUserId)

                val members = hashMapOf(
                    "users" to users,
                )

                 firebaseBD.collection("messages")
                        .document("$from-$toUserId")
                        .set(members)

                firebaseBD.collection("messages")
                    .document("$from-$toUserId")
                    .collection("messages")
                    .add(data)

            }
        }
        return ""
    }

    override suspend fun getMessage(
        toUserId: String,
        flow: MutableStateFlow<Message>
    ): StateFlow<Message> {

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

        queryToFrom?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                snapshot.forEach {
                    val message = Message(
                        message = it.data.get("message").toString(),
                        fromUserId = it.data.get("fromUserId").toString(),
                        toUserId = it.data.get("toUserId").toString(),
                        hour = it.data.get("hour").toString(),
                        messageId = it.data?.get("messageId").toString(),
                    )
                    flow.value = message
                }
            } else {
            }
        }

        queryFromTo?.addSnapshotListener { snapshot, e ->
          if (e != null) {
              return@addSnapshotListener
          }

          if (snapshot != null) {
              snapshot.forEach {
                  val message = Message(
                      message = it.data.get("message").toString(),
                      fromUserId = it.data.get("fromUserId").toString(),
                      toUserId = it.data.get("toUserId").toString(),
                      hour = it.data.get("hour").toString(),
                      messageId = it.data?.get("messageId").toString(),
                  )
                  flow.value = message
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

}