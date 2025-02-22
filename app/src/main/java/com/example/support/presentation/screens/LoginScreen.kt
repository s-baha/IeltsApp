package com.example.support.presentation.screens

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
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.twotone.Email
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
import com.example.support.presentation.screens.state.LoginScreenEvent
import com.example.support.presentation.screens.state.LoginScreenState
import com.example.support.presentation.screens.viewmodel.LoginScreenViewModel
import com.example.support.presentation.ui.component.MenuTop
import com.example.support.presentation.ui.component.StyledButton
import com.example.support.util.Result

@Composable
fun LoginScreen(
    onNavigateTo: (String) ->Unit
){
    val viewModel= hiltViewModel<LoginScreenViewModel>()

    val context = LocalContext.current
    LaunchedEffect(viewModel.state.loginResult) {
        viewModel.state.loginResult?.let { loginResult ->
            when (loginResult) {
                is Result.Success<*> -> {
                    onNavigateTo(Screen.MainMenu.route)
                }
                is Result.Failure<*> -> {
                    Toast.makeText(
                        context,
                        loginResult.msq,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    LoginView(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        onNavigateTo = onNavigateTo
    )
}

@Composable
fun LoginView(
    state: LoginScreenState = LoginScreenState(),
    onEvent: (LoginScreenEvent) ->Unit={},
    onNavigateTo: (String) -> Unit={}
){
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
                            Color(-0x767014),
                            Color(-0x767014),
                            Color(-0xa6a267)                         )
                    )
                )

        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                //title "Login"
                Text(stringResource(
                    id= R.string.login),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.White
                )

                //login email text field
                OutlinedTextField(
                value = state.email,
                onValueChange = {
                    onEvent(LoginScreenEvent.EmailUpdated(it))
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
                    .padding(top = 50.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(color = Color(0xFF6B70B4))
                )

                //login password text field
                OutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        onEvent(LoginScreenEvent.PasswordUpdated(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id=R.string.password), color = Color.White)
                    },
                    leadingIcon ={
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.Lock),
                            contentDescription = null
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xFF6B70B4))
                )
                //button for login
                StyledButton(
                    modifier = Modifier.padding(top = 120.dp),
                    onClick = { onEvent(LoginScreenEvent.LoginButtonClicked)}){
                    Text(
                        text = stringResource(id=R.string.login),
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
                //go to register page
                Text(
                    text = stringResource(id = R.string.no_account_login),
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .clickable {
                            onNavigateTo(Screen.Register.route)
                        }
                )
            }
        }
    }
    }
}