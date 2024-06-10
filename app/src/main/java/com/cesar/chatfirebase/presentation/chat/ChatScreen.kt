package com.cesar.chatfirebase.presentation.chat

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cesar.chatfirebase.MainActivity
import com.cesar.chatfirebase.R
import com.cesar.chatfirebase.ui.theme.green
import com.cesar.chatfirebase.viewModel.ChatViewModel
import com.cesar.domain.model.User
import com.example.prueba_softtek.component.DialogLoading
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
    val listState = rememberLazyListState()

    LaunchedEffect(Unit){
        viewModel.changeUserId((context as MainActivity).getUser()?.id?:"",user.id?:"")
        viewModel.getListMessage()
    }

    LaunchedEffect(Unit){
        viewModel.uiState.collect{
          when (it) {
                is ChatUiState.Error -> {
                    loading = false

                }

                is ChatUiState.Loading -> {
                    loading = true
                }

                is ChatUiState.Success -> {
                    loading = false
                }

                is ChatUiState.Nothing -> {

                }

                is ChatUiState.SuccessGetMessage -> {
                    loading = false
                    it.messageData?.let { it1 ->
                        if (it1.message != null) viewModel.updateMessages(it1)
                    }
                }

                is ChatUiState.SuccessGetListMessageLocal -> {
                    if ((it.listMessage?.size ?: 0) > 0) {
                        viewModel.updateListMessages(it.listMessage!!)
                    }
                }

                is ChatUiState.SuccessGetOnlineByUser -> {
                    loading = false
                    it.online?.let { it1 ->
                        online = it1
                    }
                }

                is ChatUiState.ErrorGetListMessage -> {
                    loading = false
                }

                is ChatUiState.SuccessGetListMessage -> {
                    loading = false
                    viewModel.getListMessageLocal()
                    viewModel.getOnlineByUser()
                    viewModel.getMessage()
                }
          }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.ui2State.collect {
            when (it) {
                is GetOnlineState.Nothing -> {

                }
                is GetOnlineState.SuccessGetOnlineByUser -> {
                    it.online?.let { it1 ->
                        online = it1
                    }
                }
            }
        }
    }

    LaunchedEffect(stateElements.messages.size) {
        if (!listState.isScrolledToTheEnd()) {
            val itmIndex = listState.layoutInfo.totalItemsCount - 1
            if (itmIndex >= 0) {
                val lastItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastItem?.let {
                    listState.animateScrollToItem(itmIndex, it.size + it.offset)
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
                                style = TextStyle(fontSize = 22.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
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
                ConstraintLayout(
                    Modifier
                        .constrainAs(layout) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                ) {
                    val (textField,icon) = createRefs()
                    OutlinedTextField(
                        value = stateElements.message,
                        onValueChange = {value->
                            viewModel.changeMessage(value)
                        },
                        modifier = Modifier.constrainAs(textField){
                            bottom.linkTo(parent.bottom)
                            top.linkTo(parent.top)
                            end.linkTo(icon.start)
                            start.linkTo(parent.start)
                            width= Dimension.fillToConstraints
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend ={
                            viewModel.sendMessage()
                            keyboardController?.hide()
                        }),
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
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .constrainAs(lazyColumn) {
                            bottom.linkTo(layout.top)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                        .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                ) {
                    stateElements.messages?.let {messages->
                        items(messages){message->
                            ItemMessage(message)
                        }
                    }
                }

            }
        }
    }
}
fun LazyListState.isScrolledToTheEnd() : Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}



