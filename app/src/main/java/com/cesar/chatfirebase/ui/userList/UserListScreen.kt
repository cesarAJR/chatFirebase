package com.cesar.chatfirebase.ui.userList

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material.icons.sharp.List
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cesar.chatfirebase.R
import com.cesar.chatfirebase.ui.register.RegisterUiState
import com.cesar.chatfirebase.ui.theme.green
import com.cesar.chatfirebase.util.getGoogleLoginAuth
import com.cesar.chatfirebase.viewModel.UserListViewModel
import com.cesar.domain.model.User
import com.example.prueba_softtek.component.DialogLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(viewModel: UserListViewModel = koinViewModel(),onChat:(User)->Unit,onLogin:()->Unit) {
    val context = LocalContext.current
    var callService by rememberSaveable {
        mutableStateOf(true)
    }

    var loading by remember {
        mutableStateOf<Boolean>(false)
    }

    LaunchedEffect( true){
        if (callService){
            viewModel.getUserList()
            callService = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {

                },
                title = {
                    Text(text = "Amigos")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = green
                ),
                actions = {
                    Row{
                        Icon(
                            imageVector = Icons.Sharp.Settings,
                            contentDescription = "list",
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable {

                                }
                        )
                        Icon(
                            imageVector = Icons.Sharp.ExitToApp,
                            contentDescription = "Information",
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable {
                                    viewModel.logout()
                                    getGoogleLoginAuth(context).signOut()
                                }
                        )
                    }

                }
            )
        }
    ) {
        Surface(modifier = Modifier
            .padding(top = it.calculateTopPadding())
            .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
            ) {
                viewModel.stateElements.users?.let {users->
                    items(users){user->
                        ItemUser(user){
                            onChat(user)
                        }
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


    LaunchedEffect(Unit){
        viewModel.uiState.collect{
            when(it){
                is UserListUiState.Error -> {
                    loading=false
                    Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UserListUiState.Loading -> {
                    loading=true
                }
                is UserListUiState.Success -> {
                    loading=false
                    it.list?.let { it1 -> viewModel.updateUserList(it1) }
                }
                is UserListUiState.Nothing -> {

                }

                is UserListUiState.SuccessLogout -> {
                    onLogin()
                }
            }
        }

    }
}

@Composable
fun ItemUser(
    user: User,
    onChat:(User)->Unit
) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 5.dp)
                .clickable(onClick = {
                    onChat(user)
                })
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo_user),
                contentDescription = "logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape)

            )
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Text(text = user.name?:"")
                Text(text = user.email?:"")
            }

        }


    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
@Preview
fun ItemUserPreview(){
    val user = User("qeqeqw21","cesar","cesar@gmail.com")
//    ItemUser(user = user)
}