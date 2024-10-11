package com.example.seniorproject_icebreaker.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorproject_icebreaker.R
import com.example.seniorproject_icebreaker.data.firestore.FirestoreQueries
import com.example.seniorproject_icebreaker.ui.theme.AppPadding
import com.example.seniorproject_icebreaker.ui.theme.IceBreakerTheme
import com.example.seniorproject_icebreaker.ui.theme.Typography
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@Composable
fun LoginScreen(navController: NavController, coroutineScope: CoroutineScope) {
    // To access current context within composable function
    val context = LocalContext.current
    // Define to interact with Firebase Authentication
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Surface to contain elements
    Surface {
        // Get the system's dark mode state
        val isDarkTheme = isSystemInDarkTheme()

        // Used to hold text field values
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // Object to manage focus within composable elements
        val focusManager = LocalFocusManager.current
        // Used to switch focus
        val focusRequesterForEmail = remember { FocusRequester() }
        val focusRequesterForPassword = remember { FocusRequester() }

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
                text = "Welcome to IceBreaker!",
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
                ))

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesterForPassword),
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

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Forget Password?",
                    modifier = Modifier
                        .clickable {
                            navController.navigate("AccountRecoveryScreen")
                        }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = if (isDarkTheme) R.drawable.google_sign_in_dark else R.drawable.google_sign_in_light),
                    contentDescription = "Google Sign In Button",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable {
                            // Jetpack API that supports multiple sign-in methods including "Sign in with Google"
                            val credentialManager = CredentialManager.create(context)

                            // Generating hashed nonce for enhanced security
                            val rawNonce = UUID
                                .randomUUID()
                                .toString()
                            val bytes = rawNonce.toByteArray()
                            val md = MessageDigest.getInstance("SHA-256")
                            val digest = md.digest(bytes)
                            val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

                            // Configure options for retrieving user's Google ID
                            val googleIdOptions: GetGoogleIdOption = GetGoogleIdOption
                                .Builder()
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId(context.getString(R.string.default_web_client_id))
                                .setNonce(hashedNonce)
                                .build()

                            // Build request
                            val request: GetCredentialRequest = GetCredentialRequest
                                .Builder()
                                .addCredentialOption(googleIdOptions)
                                .build()

                            // Launch coroutine to send API call and listen for response (Google ID Token)
                            coroutineScope.launch {
                                try {
                                    // Grab response and extract Google ID Token
                                    val result = credentialManager.getCredential(
                                        request = request,
                                        context = context,
                                    )
                                    val credential = result.credential
                                    val googleIdTokenCredential = GoogleIdTokenCredential
                                        .createFrom(credential.data)
                                    val googleIdToken = googleIdTokenCredential.idToken

                                    // Use Google ID Token to proceed with Firebase Authentication
                                    val authCredential: AuthCredential =
                                        GoogleAuthProvider.getCredential(googleIdToken, null)
                                    auth
                                        .signInWithCredential(authCredential)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // Google ID Token is valid so proceed with log in to HomeScreen
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Login Successful. You are signed in with Google.",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                                navController.navigate("HomeScreen")

                                                // Get the signed-in user
                                                val user = auth.currentUser
                                                if (user != null) {
                                                    // Get the signed-in user's uid and email
                                                    val userUid = user.uid
                                                    val userEmail = user.email

                                                    // Store user data in 'users' collection of the database if not previously set
                                                    coroutineScope.launch {
                                                        if (userEmail != null) {
                                                            // Create and add to 'users' collection in database if user is new
                                                            if (FirestoreQueries.checkUserIsUnique(
                                                                    "uid",
                                                                    userUid
                                                                )
                                                            ) {
                                                                // Defining new user data
                                                                val newUserData = hashMapOf(
                                                                    "uid" to userUid,
                                                                    "email" to userEmail,
                                                                    "profilePicture" to ""
                                                                )

                                                                // Create document
                                                                FirestoreQueries.createUsersDocument(
                                                                    newUserData
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                // Handle sign-in failure
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Authentication Failed: ${task.exception?.message}",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        }
                                } catch (e: GetCredentialException) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Login Failed: $e.message",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                } catch (e: GoogleIdTokenParsingException) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Login Failed: $e.message",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    // Check for empty input
                    when {
                        email.isEmpty() && password.isNotEmpty() -> {
                            // User failed to enter email
                            Toast.makeText(context, "Enter email.", Toast.LENGTH_SHORT).show()
                            // Request focus on the email field
                            focusRequesterForEmail.requestFocus()
                            return@Button
                        }

                        password.isEmpty() && email.isNotEmpty() -> {
                            // User failed to enter password
                            Toast.makeText(context, "Enter password.", Toast.LENGTH_SHORT).show()
                            // Request focus on the password field
                            focusRequesterForPassword.requestFocus()
                            return@Button
                        }

                        email.isEmpty() && password.isEmpty() -> {
                            // User failed to enter email and password
                            Toast.makeText(context, "Enter credentials!", Toast.LENGTH_SHORT).show()
                            // Request focus on the email field
                            focusRequesterForEmail.requestFocus()
                            return@Button
                        }

                        else -> {
                            // User provided email and password so proceed to login attempt
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        if (user != null) {
                                            if (user.isEmailVerified) {
                                                // Email is verified so proceed with log in to HomeScreen
                                                Toast.makeText(
                                                    context,
                                                    "Login Successful.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate("HomeScreen")

                                                // Store user data in 'users' collection of the database if not previously set
                                                val uid = auth.currentUser?.uid
                                                coroutineScope.launch {
                                                    if (uid != null) {
                                                        // Create and add to 'users' collection in database if user is new
                                                        if (FirestoreQueries.checkUserIsUnique(
                                                                "uid",
                                                                uid
                                                            )
                                                        ) {
                                                            // Defining new user data
                                                            val newUserData = hashMapOf(
                                                                "uid" to uid,
                                                                "email" to email,
                                                                "username" to ""
                                                            )

                                                            // Create document
                                                            FirestoreQueries.createUsersDocument(
                                                                newUserData
                                                            )
                                                        }
                                                    }
                                                }
                                            } else {
                                                // Email is not verified
                                                Toast.makeText(
                                                    context,
                                                    "Login Failed: Email is not verified. Please verify your email.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                auth.signOut()
                                            }
                                        } else {
                                            // Current user is null
                                            Toast.makeText(
                                                context,
                                                "Login Failed: Current user is null.",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    } else {
                                        // Credentials are invalid
                                        Toast.makeText(
                                            context,
                                            "Login Failed: Invalid credentials, try again.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Log In".uppercase())
            }

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    navController.navigate("RegisterScreen")
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Create an Account".uppercase())
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLoginScreen() {
    IceBreakerTheme {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        LoginScreen(navController, coroutineScope)
    }
}