package com.example.support.presentation.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StyledButton(
    modifier: Modifier,
    onClick: ()-> Unit={},
    content: @Composable () -> Unit ={}
){
    Button(
        modifier = modifier,
        onClick =onClick,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = androidx.compose.ui.graphics.Color.White
        )
    )
    {
        content()
    }

}