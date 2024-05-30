package com.cesar.chatfirebase.ui.userList

import android.content.Context
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cesar.chatfirebase.R
import com.cesar.chatfirebase.ui.theme.green
import com.cesar.chatfirebase.util.getGoogleLoginAuth
import com.cesar.chatfirebase.viewModel.UserListViewModel
import com.cesar.domain.model.User
import com.example.prueba_softtek.component.DialogLoading
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(viewModel: UserListViewModel = koinViewModel(),onChat:(User)->Unit,onLogin:()->Unit,onSetting:()->Unit) {
    val context = LocalContext.current

    val stateRefresh = rememberPullToRefreshState()
    if (stateRefresh.isRefreshing) {
        LaunchedEffect( true){
            viewModel.getUserList()
        }
    }

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
                                    onSetting()
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
            Box(Modifier.nestedScroll(stateRefresh.nestedScrollConnection)) {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                ) {
                    viewModel.stateElements.users?.let { users ->
                        items(users) { user ->
                            ItemUser(user, context) {
                                onChat(user)
                            }
                        }
                    }
                }
                PullToRefreshContainer(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = stateRefresh,
                )
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
                    stateRefresh.endRefresh()
                    loading=false
                    Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UserListUiState.Loading -> {
                    if (!stateRefresh.isRefreshing){
                        loading=true
                    }

                }
                is UserListUiState.Success -> {
                    stateRefresh.endRefresh()
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
    context:Context,
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
            var painter = painterResource(R.drawable.baseline_person_24)
            if (user.photoUrl!=null && user.photoUrl!="null" && user.photoUrl!!.isNotEmpty()){
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