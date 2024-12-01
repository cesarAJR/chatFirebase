package com.cesar.chatfirebase.presentation.userList

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun CustomFloatingActionButton(
    expandable: Boolean,
    onCreateGroup:()->Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    if (!expandable) { // Close the expanded fab if you change to non expandable nav destination
        isExpanded = false
    }


    val fabSize = 100.dp
    val expandedFabWidth by animateDpAsState(
        targetValue = if (isExpanded) 100.dp else fabSize,
        animationSpec = spring(dampingRatio = 3f)
    )

    val expandedFabHeight by animateDpAsState(
        targetValue = if (isExpanded) 58.dp else fabSize,
        animationSpec = spring(dampingRatio = 3f)
    )

    ConstraintLayout(
    ) {
        val (box,fab) = createRefs()
        Box(
            modifier = Modifier
                .offset(y = (25).dp, x = 5.dp)
                .size(
                    width = expandedFabWidth,
                    height = (animateDpAsState(
                        if (isExpanded) 80.dp else 0.dp,
                        animationSpec = spring(dampingRatio = 4f)
                    )).value
                )
                .constrainAs(box) {
                    end.linkTo(parent.end)
                    bottom.linkTo(fab.top)
                }

        ) {
            Column {
                Box(
                    modifier = Modifier
                        .background(
                            Color.Gray,
                            shape = RoundedCornerShape(18.dp)
                        ).clickable {
                            isExpanded = false
                            onCreateGroup()
                        }
                ) {
                    Text(
                        text = "Crear Grupo",
                        style = TextStyle(color = Color.White),
                        modifier = Modifier.padding(10.dp)
                       )
                }

            }
        }

        FloatingActionButton(
            onClick = {
                if (expandable) {
                    isExpanded = !isExpanded
                }
            },
            modifier = Modifier.constrainAs(fab) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        ) {
            Icon(Icons.Sharp.Add,"agregar")
        }
    }
}



