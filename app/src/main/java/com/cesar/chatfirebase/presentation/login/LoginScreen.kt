package com.cesar.chatfirebase.presentation.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cesar.chatfirebase.MainActivity
import com.cesar.chatfirebase.R
import com.cesar.chatfirebase.util.AuthResultContract
import com.cesar.chatfirebase.util.getGoogleLoginAuth
import com.cesar.chatfirebase.viewModel.LoginViewModel
import com.cesar.domain.model.User
import com.example.prueba_softtek.component.DialogLoading
import com.google.android.gms.common.api.ApiException
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel(), onRegisterUser:()->Unit, onUserList:()->Unit) {
    val stateElements = viewModel.stateElements
    val context = LocalContext.current
    val activity = (LocalContext.current as MainActivity)
    activity.softInputUnspecified()
    val signInRequestCode = 1
    val user by remember {
        mutableStateOf("")
    }

    val password by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf<Boolean>(false)
    }


    val interactionSourceUser = remember { MutableInteractionSource() }
    val isFocusedUser by interactionSourceUser.collectIsFocusedAsState()

    val interactionSourcePassword = remember { MutableInteractionSource() }
    val isFocusedPassword by interactionSourcePassword.collectIsFocusedAsState()

    val IndicatorUnfocusedWidth = 1.dp
    val IndicatorFocusedWidth = 3.dp
    val TextFieldPadding = 16.dp

    val indicatorColor = Color.White
    val indicatorWidthUser = if (isFocusedUser) IndicatorFocusedWidth else IndicatorUnfocusedWidth
    val fontSizeLabelUser = if (isFocusedUser || user.isNotEmpty()) 12.sp else 18.sp
    val indicatorWidthPassword = if (isFocusedPassword) IndicatorFocusedWidth else IndicatorUnfocusedWidth
    val fontSizeLabelPassword = if (isFocusedPassword || password.isNotEmpty()) 12.sp else 18.sp

    val authResultLauncher = rememberLauncherForActivityResult(contract = AuthResultContract()){task->
        try {
            val account = task?.getResult(ApiException::class.java)
            if (account == null){
                Toast.makeText(context,"Ocurrio un error",Toast.LENGTH_SHORT).show()
            }else{
                account.idToken?.let {token->
                    viewModel.loginGoogle(token)
                }
            }
        }catch (e:ApiException){
            Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.back_login),
                contentScale = ContentScale.FillBounds
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val scope = rememberCoroutineScope()
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "potencie"
        )
        Text(
            text = "Bienvenido",
            fontSize = 40.sp,
            style = TextStyle(color = Color.White),
            textAlign = TextAlign.Center
        )
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
            value = stateElements.user,
            onValueChange = {
                viewModel.changeUser(it)
            },
            enabled = true,
            interactionSource = interactionSourceUser,
            textStyle = TextStyle(
                color = Color.White,
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
                    text = "Correo",
                    color = Color.White,
                    fontSize = fontSizeLabelUser
                )
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .drawBehind {
                    val strokeWidth = indicatorWidthPassword.value * density
                    val y = (size.height - strokeWidth / 2) - 15
                    drawLine(
                        indicatorColor,
                        Offset(TextFieldPadding.toPx(), y),
                        Offset(size.width - TextFieldPadding.toPx(), y),
                        strokeWidth
                    )
                }
            ,
            value = stateElements.password,
            onValueChange = {
                viewModel.changePassword(it)
            },
            enabled = true,
            interactionSource = interactionSourcePassword,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 18.sp
            ),
            visualTransformation =  PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor =  Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            label = {
                Text(
                    "Contraseña",
                    color = Color.White,
                    fontSize = fontSizeLabelPassword
                )
            },
            trailingIcon = {

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
                viewModel.login()
            }
        ) {
            Text(
                text = "Ingresar",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(10),
            border = BorderStroke(1.dp,Color(android.graphics.Color.parseColor("#FFFFFF"))),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(android.graphics.Color.parseColor("#000000"))
            ),
            onClick = {
                authResultLauncher.launch(signInRequestCode)
            }
        ) {
            Row(
                Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.ic_logo_google),
                    contentDescription = "potencie"
                )
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    text = "Ingresar con Google",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        TextButton(
            onClick = {
                      onRegisterUser()
            },
            ) {
            Text(

                textAlign = TextAlign.Center,
                text = "Crear Cuenta",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                ),
                textDecoration = TextDecoration.Underline
            )
        }

        if (loading){
            DialogLoading(true)
        }else{
            DialogLoading(false)
        }

        LaunchedEffect(Unit){
            viewModel.uiState.collect{
                when(it){
                    is LoginUiState.Error -> {
                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                        loading = false
                    }
                    is LoginUiState.Loading -> {
                        loading = true
                    }
                    is LoginUiState.Success -> {
                        activity.updateOnline(true)
                        onUserList()
                        loading = false
                        viewModel.cleanData()
                    }
                    is LoginUiState.Nothing -> {

                    }

                    is LoginUiState.SuccessLoginGoogle -> {
                        activity.updateOnline(true)
                         onUserList()
                        loading = false
                        viewModel.cleanData()
                    }

                    is LoginUiState.ErrorLoginGoogle -> {
                        loading = false
                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                        getGoogleLoginAuth(context).signOut()
                    }
                }
            }
        }
    }
}





