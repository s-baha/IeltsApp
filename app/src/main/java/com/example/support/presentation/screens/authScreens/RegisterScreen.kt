package com.example.support.presentation.screens.authScreens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.support.R
import com.example.support.presentation.navigation.Screen
import com.example.support.presentation.screens.state.RegisterScreenEvent
import com.example.support.presentation.screens.state.RegisterScreenState
import com.example.support.presentation.viewModels.authViewModels.RegisterScreenViewModel
import com.example.support.presentation.ui.component.MenuTop
import com.example.support.presentation.ui.component.StyledButton
import com.example.support.util.Result


@Composable
fun RegisterScreen(
    onNavigateTo: (String) ->Unit,
    onExitGame: () -> Unit
){
    val viewModel= hiltViewModel<RegisterScreenViewModel>()
    val context = LocalContext.current
    LaunchedEffect(viewModel.state.registerResult) {
        viewModel.state.registerResult?.let { registerResult ->
            when (registerResult) {
                is Result.Success<*> -> {
                    onNavigateTo(Screen.MainMenu.route)
                }
                is Result.Failure<*> -> {
                    Toast.makeText(
                        context,
                        registerResult.msq,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    RegisterView(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        onNavigateTo=onNavigateTo
    )
}


@Composable
fun RegisterView(
    state: RegisterScreenState=RegisterScreenState(),
    onEvent: (RegisterScreenEvent)->Unit = {},
    onNavigateTo: (String) -> Unit={}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // hide keyboard when touch
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                    }
                )
            }
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFF7F8AD4), Color(0xFF1E244D))
                )
            ),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MenuTop()
        Spacer(modifier = Modifier.padding(35.dp))
        //box for design
        Box(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .fillMaxHeight(0.8f)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(-0x767014),  // 0%
                            Color(-0x767014),  // 58%
                            Color(-0xa6a267) // 100%
                        )
                    )
                )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                //title "Register"
                Text(
                    stringResource(
                        id = R.string.register
                    ),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.White
                )
                // register username text field
                OutlinedTextField(
                    value = state.username,
                    onValueChange = {
                     onEvent(RegisterScreenEvent.UsernameUpdated(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id=R.string.username), color = Color.White)
                    },
                    leadingIcon ={
                        Icon(
                            painter = rememberVectorPainter(image = Icons.TwoTone.AccountCircle),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xFF6B70B4))
                )
                //register email text field
                OutlinedTextField(
                    value = state.email,
                    onValueChange = {
                        onEvent(RegisterScreenEvent.EmailUpdated(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id=R.string.email), color = Color.White)
                    },
                    leadingIcon ={
                        Icon(
                            painter = rememberVectorPainter(image = Icons.TwoTone.Email),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xFF6B70B4))
                )

                //register password text field
                OutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        onEvent(RegisterScreenEvent.PasswordUpdated(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id=R.string.password), color = Color.White)
                    },
                    leadingIcon ={
                        Icon(
                            painter = rememberVectorPainter(image = Icons.TwoTone.Lock),
                            contentDescription = null
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xFF6B70B4))
                )


                //button for register
                StyledButton(
                    modifier = Modifier.padding(top = 45.dp),
                    onClick = {onEvent(RegisterScreenEvent.RegisterButtonClicked) }
                ) {
                    Text(
                        text = stringResource(id = R.string.register),
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
                //go to login page
                Text(
                    text = stringResource(id = R.string.no_account_register),
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .clickable {
                            onNavigateTo(Screen.Login.route)
                        }
                )
            }
        }
    }
    }
}