package com.example.seniorproject_icebreaker.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(navController: NavController) {
    // To access current context within composable function
    val context = LocalContext.current
    // Define to interact with Firebase Authentication
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Surface to contain elements
    Surface {
        // Get the system's dark mode state
        val isDarkTheme = isSystemInDarkTheme()

        // State to hold text field values
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        // Object to manage focus within composable elements
        val focusManager = LocalFocusManager.current
        // Used to switch focus
        val focusRequesterForEmail = remember { FocusRequester() }
        val focusRequesterForPassword = remember { FocusRequester() }
        val focusRequesterForConfirmPassword = remember { FocusRequester() }

        // Column to arrange elements vertically
        Column(
            // Fill parent space and assign padding
            modifier = Modifier
                .fillMaxSize()
                .padding(AppPadding.large),
            // Center vertically and horizontally
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = if (isDarkTheme) R.drawable.app_banner_polar_blue_dark else R.drawable.app_banner_polar_blue_light),
                contentDescription = "App Logo",
                modifier = Modifier.size(300.dp)
            )

            Text(
                text = "Create an Account!",
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(35.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesterForEmail),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesterForPassword),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("confirm password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesterForConfirmPassword),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    // Check for empty input
                    when {
                        email.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Registration Failed: Fill all fields.",
                                Toast.LENGTH_SHORT
                            ).show()
                            focusRequesterForEmail.requestFocus()
                            return@Button
                        }

                        password.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Registration Failed: Fill all fields.",
                                Toast.LENGTH_SHORT
                            ).show()
                            focusRequesterForPassword.requestFocus()
                            return@Button
                        }

                        confirmPassword.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Registration Failed: Fill all fields.",
                                Toast.LENGTH_SHORT
                            ).show()
                            focusRequesterForConfirmPassword.requestFocus()
                            return@Button
                        }
                    }

                    // Verify password confirmation matches
                    if (password != confirmPassword) {
                        Toast.makeText(
                            context,
                            "Registration Failed: Passwords do not match.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    // Attempt to create user
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Define current user
                                val user = auth.currentUser

                                // Registration succeeded so proceed to email verification
                                Toast.makeText(
                                    context,
                                    "Registration Successful: Verification email sent to $email",
                                    Toast.LENGTH_SHORT
                                ).show()
                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener { verificationTask ->
                                        if (verificationTask.isSuccessful) {
                                            // Email verification sent successfully so sign out user and proceed to LoginScreen to enforce verification
                                            auth.signOut()
                                            navController.navigate("LoginScreen")
                                        } else {
                                            // Failed to send verification email
                                            Toast.makeText(
                                                context,
                                                "Registration Failed: Unable to send verification email.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                // Registration failed
                                Toast.makeText(
                                    context,
                                    "Registration Failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Sign Up".uppercase())
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewRegisterScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        RegisterScreen(navController)
    }
}