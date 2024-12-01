package com.cesar.chatfirebase.presentation.createGroup

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cesar.chatfirebase.R
import com.cesar.chatfirebase.ui.theme.green

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen() {
    val name by remember {
        mutableStateOf("")
    }
    val interactionSourceName = remember { MutableInteractionSource() }
    val isFocusedName by interactionSourceName.collectIsFocusedAsState()
    val fontSizeLabelName = if (isFocusedName || name.isNotEmpty()) 12.sp else 18.sp

    val IndicatorUnfocusedWidth = 1.dp
    val IndicatorFocusedWidth = 3.dp
    val TextFieldPadding = 16.dp

    val indicatorColor = Color.White
    val indicatorWidthName= if (isFocusedName) IndicatorFocusedWidth else IndicatorUnfocusedWidth

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {

                        }
                    ){
                        Image(imageVector = Icons.Sharp.KeyboardArrowLeft, contentDescription = "" )
                    }
                },
                title = {
                    Text(text = "Crear Grupo")
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
            modifier = Modifier
                .fillMaxSize()
            ) {
                val (layout,lazyColumn) = createRefs()

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .drawBehind {
                            val strokeWidth = indicatorWidthName.value * density
                            val y = (size.height - strokeWidth / 2) - 15
                            drawLine(
                                indicatorColor,
                                Offset(TextFieldPadding.toPx(), y),
                                Offset(size.width - TextFieldPadding.toPx(), y),
                                strokeWidth
                            )
                        }
                    ,
                    value = "",
                    onValueChange = {
                       ""
                    },
                    enabled = true,
                    interactionSource = interactionSourceName,
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
                            text = "Nombre del grupo",
                            color = Color.White,
                            fontSize = fontSizeLabelName
                        )
                    },
                    singleLine = true
                )
            }
        }
    }

}