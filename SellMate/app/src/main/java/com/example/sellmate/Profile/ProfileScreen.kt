package com.example.sellmate.Profile



import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.sellmate.R
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.sellmate.viewmodel.AuthViewModel

fun simpanGambarProfile(context: Context, uri: Uri?) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("profile_image_uri", uri?.toString()) // Menyimpan URI gambar
    editor.apply()
}

// Fungsi untuk memuat URI gambar dari SharedPreferences
fun muatGambarProfile(context: Context): Uri? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
    val uriString = sharedPreferences.getString("profile_image_uri", null)
    return if (uriString != null) Uri.parse(uriString) else null
}
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf<Uri?>(muatGambarProfile(context)) }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Launcher to pick image from gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
        profileBitmap = null // Reset the camera bitmap if gallery is chosen
        simpanGambarProfile(context, uri) // Save the image URI to SharedPreferences
    }

    // Launcher to take a photo using the camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        profileBitmap = bitmap
        profileImageUri = null // Reset the gallery URI if camera is used
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with Logo and Text
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                // Here you can place your logo image, use placeholder if necessary
                Image(
                    painter = painterResource(id = R.drawable.sellmate), // Replace with your logo
                    contentDescription = "Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // SellMate Text
            Text(
                text = "SellMate",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Picture Section (adjusted position)
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable { showImagePickerDialog = true } // Open the image picker dialog when clicked
        ) {
            when {
                profileBitmap != null -> {
                    // Display image from the camera
                    Image(
                        bitmap = profileBitmap!!.asImageBitmap(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                profileImageUri != null -> {
                    // Display image from the gallery
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    // Placeholder image when no image is selected
                    Image(
                        painter = painterResource(id = R.drawable.profile), // Use your placeholder image
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Edit Icon
            Icon(
                painter = painterResource(id = R.drawable.ic_edit), // Your edit icon
                contentDescription = "Edit Profile Picture",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
                    .clickable { showImagePickerDialog = true },
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User's Name
        Text(text = "Istya Yulia Amesti", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Section
        Text(
            text = "Settings",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for actions like changing name, email, password, etc.
        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
        val buttonColors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE6D4A4))

        Button(
            onClick = { /* Action to change name */ },
            modifier = buttonModifier,
            colors = buttonColors,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Nama", fontSize = 16.sp, color = Color.Black)
        }

        Button(
            onClick = { /* Action to change email */ },
            modifier = buttonModifier,
            colors = buttonColors,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Ubah Email", fontSize = 16.sp, color = Color.Black)
        }

        Button(
            onClick = { /* Action to change password */ },
            modifier = buttonModifier,
            colors = buttonColors,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Ubah Password", fontSize = 16.sp, color = Color.Black)
        }

        Button(
            onClick = { showLogoutDialog = true }, // Show logout confirmation dialog
            modifier = buttonModifier,
            colors = buttonColors,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Log Out", fontSize = 16.sp, color = Color.Black)
        }

        // Dialog for image source picker
        if (showImagePickerDialog) {
            AlertDialog(
                onDismissRequest = { showImagePickerDialog = false },
                title = { Text("Pilih Sumber Gambar") },
                buttons = {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Button(
                            onClick = {
                                cameraLauncher.launch(null)
                                showImagePickerDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ambil Foto")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                galleryLauncher.launch("image/*")
                                showImagePickerDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Pilih dari Galeri")
                        }
                    }
                }
            )
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(
                        text = "Konfirmasi Logout",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                },
                text = {
                    Text(
                        text = "Apakah Anda yakin ingin keluar?",
                        style = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            // Logic to handle logout
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text(text = "Iya", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showLogoutDialog = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                    ) {
                        Text(text = "Tidak", color = Color.White)
                    }
                }
            )
        }
    }
}
