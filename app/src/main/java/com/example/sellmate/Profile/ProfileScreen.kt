package com.example.sellmate.Profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.sellmate.R
import androidx.navigation.NavController
import com.example.sellmate.viewmodel.AuthViewModel

fun saveProfileImage(context: Context, uri: Uri?) {
    val sharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("profile_image_uri", uri?.toString()).apply()
}

fun loadProfileImage(context: Context): Uri? {
    val sharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("profile_image_uri", null)?.let { Uri.parse(it) }
}

fun handleLogout(context: Context, navController: NavController) {
    val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
    navController.navigate("landing") {
        popUpTo("profile") { inclusive = true }
    }
}

@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf(loadProfileImage(context)) }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // User data states
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    // Load user data from ViewModel
    LaunchedEffect(Unit) {
        authViewModel.loadUserData { userData ->
            userName = userData.name
            userEmail = userData.email
            userPassword = userData.password
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        profileImageUri = uri
        profileBitmap = null
        saveProfileImage(context, uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SellMate",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Text(
                text = "Logout",
                modifier = Modifier.clickable { showLogoutDialog = true },
                style = TextStyle(color = Color.Blue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        // Profile picture
        Box(modifier = Modifier.size(240.dp)) {
            when {
                profileBitmap != null -> Image(
                    bitmap = profileBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                profileImageUri != null -> Image(
                    painter = rememberAsyncImagePainter(profileImageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                else -> Image(
                    painter = painterResource(id = R.drawable.foto),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()
                        .clickable { galleryLauncher.launch("image/*") },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth() // Memenuhi lebar parent
                .padding(start = 8.dp) // Memberikan padding ke kiri
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Editable user data
        Column(modifier = Modifier.fillMaxWidth()) {
            EditableProfileItem(
                label = "Name",
                value = userName,
                leadingIcon = Icons.Default.Person,
                onValueChange = { userName = it },
                onSave = { authViewModel.updateUserData("name", it) }
            )
            EditableProfileItem(
                label = "Email",
                value = userEmail,
                leadingIcon = Icons.Default.Email,
                isEditable = false
            )
            EditableProfileItem(
                label = "Password",
                value = "********",
                leadingIcon = Icons.Default.Lock,
                isEditable = false
            )
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Apakah Anda yakin ingin keluar dari akun $userEmail") },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            handleLogout(context, navController)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                    ) {
                        Text("Ya", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showLogoutDialog = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                    ) {
                        Text("Batal", color = Color.White)
                    }
                },
                backgroundColor = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun EditableProfileItem(
    label: String,
    value: String,
    leadingIcon: ImageVector,
    isEditable: Boolean = true,
    onValueChange: (String) -> Unit = {},
    onSave: (String) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isEditing) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = {
                onSave(value)
                isEditing = false
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
            }
        } else {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = leadingIcon, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = label, style = TextStyle(fontSize = 14.sp, color = Color.Gray))
                    Text(text = value, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                }
            }
            if (isEditable) {
                IconButton(onClick = { isEditing = true }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }
    }
}
