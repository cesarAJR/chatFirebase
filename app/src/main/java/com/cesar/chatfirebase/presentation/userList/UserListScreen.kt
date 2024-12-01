package com.cesar.chatfirebase.presentation.userList

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cesar.chatfirebase.ui.theme.green
import com.cesar.chatfirebase.util.getGoogleLoginAuth
import com.cesar.chatfirebase.viewModel.UserListViewModel
import com.cesar.domain.model.User
import com.example.prueba_softtek.component.DialogLoading
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(viewModel: UserListViewModel = koinViewModel(),onChat:(User)->Unit,onLogin:()->Unit,onSetting:()->Unit,onCreateGroup:()->Unit) {
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
                            imageVector = Icons.Sharp.Refresh,
                            contentDescription = "update",
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable {
                                    viewModel.getUserList()
                                }
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Sharp.Settings,
                            contentDescription = "list",
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable {
                                    onSetting()
                                }
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Sharp.ExitToApp,
                            contentDescription = "logout",
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
        },
        floatingActionButton = {
          CustomFloatingActionButton(true,onCreateGroup)
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