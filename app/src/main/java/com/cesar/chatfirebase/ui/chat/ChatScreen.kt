package com.cesar.chatfirebase.ui.chat

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.simulateHotReload
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cesar.chatfirebase.MainActivity
import com.cesar.chatfirebase.R
import com.cesar.chatfirebase.ui.login.LoginUiState
import com.cesar.chatfirebase.ui.theme.green
import com.cesar.chatfirebase.ui.userList.ItemUser
import com.cesar.chatfirebase.viewModel.ChatViewModel
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.example.prueba_softtek.component.DialogLoading
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(viewModel:ChatViewModel= koinViewModel(),user: User,onUserList:()->Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val stateElements = viewModel.stateElements
    val context =  LocalContext.current
    (LocalContext.current as MainActivity).softInputResize()
    var loading by remember {
        mutableStateOf<Boolean>(false)
    }

    var online by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit){
        viewModel.changeUserId("",user.id?:"")
        viewModel.getListMessage()
    }

    LaunchedEffect(Unit){
        viewModel.uiState.collect{
            when(it){
                is ChatUiState.Error -> {
                    loading =  false
                }
                is ChatUiState.Loading -> {
                    loading =  true
                }
                is ChatUiState.Success -> {
//                    viewModel.changeMessage("")
                }
                is ChatUiState.Nothing -> {

                }

                is ChatUiState.SuccessGetMessage -> {
                    it.messageData?.let { it1 ->
                        if (it1.message!=null)  viewModel.updateMessages(it1)
                    }
                }

                is ChatUiState.SuccessGetListMessage -> {
                    loading =  false
                    if ((it.listMessage?.size ?: 0) > 0){
                        viewModel.updateListMessages(it.listMessage!!)
                    }
                    viewModel.getMessage()
                    viewModel.getOnlineByUser()
                }

                is ChatUiState.SuccessGetOnlineByUser ->{
                    it.online?.let { it1 ->
                        online= it1
                    }
                }
            }
        }
    }

    if (loading){
        DialogLoading(true)
    }else{
        DialogLoading(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onUserList()
                        }
                    ){
                        Image(imageVector = Icons.Sharp.KeyboardArrowLeft, contentDescription = "" )
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var painter = painterResource(R.drawable.baseline_person_24)

                        if (user.photoUrl!=null && user.photoUrl!="null" && user.photoUrl!!.isNotEmpty()){
                            val replaceString = user.photoUrl!!.substringAfterLast("/o/").replace("/","%2F")
                            val url =  "${user.photoUrl!!.substringBeforeLast("/o/")}/o/${replaceString}"
                            user.photoUrl = url
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .data(Uri.parse(user.photoUrl))
                                    .placeholder(R.drawable.baseline_person_24)
                                    .build()
                            )
                        }

                        Image(
                            painter = painter,
                            contentDescription = "logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Black, CircleShape)

                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = user.name?:"",
                                style = TextStyle(fontSize = 22.sp)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            if (online.isNotEmpty()){
                                Text(
                                    text = if (online=="true") {"En Linea"}else{"Desconectado"},
                                    style = TextStyle(fontSize = 14.sp)
                                )
                            }
                        }

                    }

                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = green
                ),
                actions = {

                }
            )
        }
    ){
        Surface(modifier = Modifier
            .padding(top = it.calculateTopPadding())
            .fillMaxSize()
        ) {
            ConstraintLayout(
                Modifier.fillMaxWidth()
            ) {
                val (layout,lazyColumn) = createRefs()
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.constrainAs(lazyColumn){
                        bottom.linkTo(layout.top)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        width= Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                ) {
                    stateElements.messages?.let {messages->
                        items(messages){message->
                            ItemMessage(message)
                        }
                    }
                }
                ConstraintLayout(
                    Modifier
                        .constrainAs(layout) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                ) {
                    val (textField,icon) = createRefs()
                    OutlinedTextField(
                        value = stateElements.message,
                        onValueChange = {
                            viewModel.changeMessage(it)
                        },
                        modifier = Modifier.constrainAs(textField){
                            bottom.linkTo(parent.bottom)
                            top.linkTo(parent.top)
                            end.linkTo(icon.start)
                            start.linkTo(parent.start)
                            width= Dimension.fillToConstraints
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(20.dp,),
                        textStyle = TextStyle(color = Color.White, fontSize = 18.sp)
                    )
                    IconButton(
                        onClick = {
                                viewModel.sendMessage()
                            keyboardController?.hide()
                        },
                        modifier =Modifier.constrainAs(icon){
                            bottom.linkTo(parent.bottom)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                    ){
                        Image(
                            imageVector = Icons.Sharp.Send,
                            contentDescription = "send",
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(green)
                        )

//                        Image(imageVector = Icons.Sharp.KeyboardArrowLeft, contentDescription = "" )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemMessage(message: Message){
        Column {
            ConstraintLayout(
                Modifier.fillMaxWidth()
            ) {
                val (layout1,layout2) = createRefs()

                if (message.fromUserId.equals((LocalContext.current as MainActivity).firebaseAuth.uid)){
                    Surface(
                        modifier = Modifier
                            .constrainAs(layout1) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            }
                            .padding(start = 50.dp),
                        shape = RoundedCornerShape(10.dp,),
                        color = Color(android.graphics.Color.parseColor("#6EAA5E"))
                    ) {
                        ConstraintLayout(
                            Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            val (textMessage,textHour) = createRefs()
                            Text(
                                modifier = Modifier.constrainAs(textMessage){
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    width= Dimension.fillToConstraints
                                },
                                style = TextStyle(color=Color.White, fontSize = 18.sp),
                                text = message.message?:""
                            )
                            Text(
                                modifier = Modifier.constrainAs(textHour){
                                    top.linkTo(textMessage.bottom)
                                    end.linkTo(parent.end)
                                    width= Dimension.fillToConstraints
                                },
                                style = TextStyle(color=Color(android.graphics.Color.parseColor("#BDC3C7")), fontSize = 12.sp),
                                text = message.hour?:""
                            )
                        }
                    }
                }else{
                    Surface(
                        modifier = Modifier
                            .constrainAs(layout2) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                width = Dimension.fillToConstraints
                            }
                            .padding(end = 50.dp),
                        shape = RoundedCornerShape(10.dp,),
                        color = Color(android.graphics.Color.parseColor("#1A5276"))
                    ) {
                        ConstraintLayout(
                            Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            val (textMessage,textHour) = createRefs()
                            Text(
                                modifier = Modifier.constrainAs(textMessage){
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    width= Dimension.fillToConstraints
                                },
                                style = TextStyle(color=Color.White, fontSize = 18.sp),
                                text = message.message?:""
                            )
                            Text(
                                modifier = Modifier.constrainAs(textHour){
                                    top.linkTo(textMessage.bottom)
                                    end.linkTo(parent.end)
                                    width= Dimension.fillToConstraints
                                },
                                style = TextStyle(color=Color(android.graphics.Color.parseColor("#BDC3C7")), fontSize = 12.sp),
                                text = message.hour?:""
                            )
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
        }
}

