package com.cesar.chatfirebase.presentation.chat


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cesar.chatfirebase.MainActivity
import com.cesar.chatfirebase.R
import com.cesar.domain.model.Message
import androidx.compose.ui.graphics.Color

@Composable
fun ItemMessage(message: Message) {
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
                    shape = RoundedCornerShape(10.dp),
                    color = Color(android.graphics.Color.parseColor("#6EAA5E"))
                ) {
                    ConstraintLayout(
                        Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        val (textMessage,textHour,ivPending) = createRefs()
                        Text(
                            modifier = Modifier.constrainAs(textMessage){
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                width= Dimension.fillToConstraints
                            },
                            style = TextStyle(color= androidx.compose.ui.graphics.Color.White, fontSize = 18.sp),
                            text = message.message?:""
                        )
                        Text(
                            modifier = Modifier.constrainAs(textHour){
                                top.linkTo(textMessage.bottom)
                                if (message.pendingSync==true) end.linkTo(ivPending.start)
                                else end.linkTo(parent.end)

                                width= Dimension.fillToConstraints
                            },
                            style = TextStyle(color= Color(android.graphics.Color.parseColor("#BDC3C7")), fontSize = 12.sp),
                            text = message.hour?:""
                        )
                        if (message.pendingSync==true){
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_pending),
                                contentDescription = "pending",
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(start = 5.dp)
                                    .constrainAs(ivPending){
                                        top.linkTo(textHour.top)
                                        end.linkTo(parent.end)
                                    }
                            )
                        }
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
                            style = TextStyle(color= androidx.compose.ui.graphics.Color.White, fontSize = 18.sp),
                            text = message.message?:""
                        )
                        Text(
                            modifier = Modifier.constrainAs(textHour){
                                top.linkTo(textMessage.bottom)
                                end.linkTo(parent.end)
                                width= Dimension.fillToConstraints
                            },
                            style = TextStyle(color= Color(android.graphics.Color.parseColor("#BDC3C7")), fontSize = 12.sp),
                            text = message.hour?:""
                        )
                    }
                }
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}