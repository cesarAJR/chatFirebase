package com.cesar.chatfirebase.presentation.userList

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cesar.chatfirebase.R
import com.cesar.domain.model.User

@Composable
fun ItemUser(user: User,
             context: Context,
             onChat:(User)->Unit) {
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
                .border(1.dp,
                    if (isSystemInDarkTheme()) Color.White else Color.Black,
                    CircleShape
                )

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