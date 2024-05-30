package com.cesar.chatfirebase.presentation.editUser

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cesar.chatfirebase.MainActivity
import com.cesar.chatfirebase.R
import com.cesar.chatfirebase.ui.theme.green
import com.cesar.chatfirebase.util.getRealPathFromUri
import com.cesar.chatfirebase.viewModel.EditUserViewModel
import com.example.prueba_softtek.component.DialogLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(viewModel: EditUserViewModel = koinViewModel(),navController: NavController, onList:()->Unit,onCamera:()->Unit) {
    val context = LocalContext.current
    val user = (LocalContext.current as MainActivity).getUser()
    val stateElements = viewModel.stateElements

    val userLabel by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf<Boolean>(false)
    }

    val interactionSourceUser = remember { MutableInteractionSource() }
    val isFocusedUser by interactionSourceUser.collectIsFocusedAsState()

    val IndicatorUnfocusedWidth = 1.dp
    val IndicatorFocusedWidth = 3.dp
    val TextFieldPadding = 16.dp
    val indicatorColor = Color.Black
    val indicatorWidthUser = if (isFocusedUser) IndicatorFocusedWidth else IndicatorUnfocusedWidth
    val fontSizeLabelUser = if (isFocusedUser || userLabel.isNotEmpty()) 12.sp else 18.sp

    val secondScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("PHOTO_URI","")


    LaunchedEffect(Unit){
        var uri: Uri?=null
        viewModel.changeUser(user!!)
        if(user.photoUrl!=null && user.photoUrl != "null"){
            uri = Uri.parse(user.photoUrl)
        }
        viewModel.changeData(uri, user.name?:"")
    }

    LaunchedEffect(Unit) {
        secondScreenResult?.value?.let {
            if (it.isNotEmpty()){
               val uri = Uri.parse(it)
                val path = getRealPathFromUri(context,uri)
                viewModel.changePhotoUri(uri,path)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiState.collect {
            when(it){
                is EditUserUiState.Error -> {
                    loading = false
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                }
                is EditUserUiState.Loading -> {
                    loading = true
                }
                is EditUserUiState.Nothing -> {

                }
                is EditUserUiState.Success -> {
                    loading = false
                   Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> {
                    onCamera()
                }
                else -> {
                    println("Permiso Denegado")
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
                            onList()
                        }
                    ){
                        Image(imageVector = Icons.Sharp.KeyboardArrowLeft, contentDescription = "" )
                    }
                },
                title = {
                    Text(
                        text = "Perfil"
                    )
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
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ConstraintLayout {
                    val (layout,image) = createRefs()
                    if (stateElements.photoUri!=null){
                        ConstraintLayout(
                            Modifier.constrainAs(layout){
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                        ) {
                            val (asyncImage,image) = createRefs()
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(stateElements.photoUri)
                                    .placeholder(LocalContext.current.getDrawable(R.drawable.baseline_person_24))
                                    .crossfade(2000)
                                    .build(),
                                contentDescription = "Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .constrainAs(asyncImage) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                    }
                            )

                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_camera_24_white),
                                contentDescription = "Image",
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .constrainAs(image) {
                                        bottom.linkTo(asyncImage.bottom)
                                        end.linkTo(asyncImage.end)
                                    }
                                    .padding(top = 10.dp)
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.DarkGray)
                                    .clickable {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                            )
                        }

                    }else{
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_camera),
                            contentDescription = "Image",
                            contentScale = ContentScale.Inside,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .clickable {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                                .constrainAs(image) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(35.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .drawBehind {
                            val strokeWidth = indicatorWidthUser.value * density
                            val y = (size.height - strokeWidth / 2) - 15
                            drawLine(
                                indicatorColor,
                                Offset(TextFieldPadding.toPx(), y),
                                Offset(size.width - TextFieldPadding.toPx(), y),
                                strokeWidth
                            )
                        }
                    ,
                    value =stateElements.name?:"",
                    onValueChange = {
                        viewModel.changeName(it)
                    },
                    enabled = true,
                    interactionSource = interactionSourceUser,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor =  Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    label = {
                        Text(
                            text = "Nombre",
                            color = Color.Black,
                            fontSize = fontSizeLabelUser
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(50.dp))
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 30.dp),
                    shape = RoundedCornerShape(10),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(android.graphics.Color.parseColor("#0072CF"))
                    ),
                    onClick = {
                        viewModel.editUser()
                    }
                ) {
                    Text(
                        text = "Guardar",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}
