package com.example.ecommerceapp.screen.profile

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerceapp.R
import com.example.ecommerceapp.components.BasicAlert
import com.example.ecommerceapp.components.ButtonComponent
import com.example.ecommerceapp.components.CenterTopAppBar
import com.example.ecommerceapp.components.TextComponent
import com.example.ecommerceapp.components.TextTermCondition
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.utils.rememberImageBitmapFromUri
import com.example.ecommerceapp.utils.uriToFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileRoute(
    onNavigateToHome: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val launcherGallery = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.setImageUri(it)
            viewModel.setUserImage(uriToFile(context, it))
        }
    }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val launcherCamera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri.value != null) {
            viewModel.setImageUri(cameraImageUri.value)  // Update ViewModel’s state with URI
            viewModel.setUserImage(uriToFile(context, cameraImageUri.value!!))
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = viewModel.createImageFileAndUri(context)
            cameraImageUri.value = uri  // Save locally so launcherCamera callback can access
            launcherCamera.launch(uri)
        } else {
            // Show error or snackbar
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    BasicAlert(
        openDialog = uiState.showImageDialog,
        onDismiss = {viewModel.hideDialog()},
        onLaunchCamera = {
            viewModel.hideDialog()
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val uri = viewModel.createImageFileAndUri(context)
                cameraImageUri.value = uri  // Save locally so launcherCamera callback can access
                launcherCamera.launch(uri)
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        onLaunchGallery = {
            viewModel.hideDialog()
            launcherGallery.launch("image/*")
        }
    )

    // 🔁 Navigate when login is successful
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToHome()
        }
    }

    ProfileScreen (
        uiState = uiState,
        onImageClick = { viewModel.showDialog() },
        onUploadClick = {
            keyboardController?.hide()
            viewModel.onSetProfileClick()
        },
        onNameChange = { viewModel.setUserName(it)},
        snackBarMessageShown = { viewModel.snackBarMessageShown() },
        snackBarHostState = snackBarHostState
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onImageClick: () -> Unit,
    onUploadClick: () -> Unit,
    onNameChange: (String) -> Unit,
    snackBarMessageShown : () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            CenterTopAppBar(R.string.profile)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) {
        ProfileContent(
            name = uiState.userName,
            imageUri = uiState.imageUri,
            modifier = Modifier.padding(it),
            onImageClick = onImageClick,
            onNameChange = onNameChange,
            onUploadClick = onUploadClick
        )
    }
    if (!uiState.userMessage.isNullOrBlank()) {
        LaunchedEffect(uiState.userMessage) {
            snackBarHostState.showSnackbar(uiState.userMessage)
            snackBarMessageShown() // Called after snackbar hides
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    name : String,
    imageUri : Uri?,
    onImageClick: () -> Unit,
    onUploadClick: () -> Unit,
    onNameChange: (String) -> Unit
){
    Column (
        modifier = modifier.background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize().padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Circle Image
        Column(modifier = Modifier
            .padding(24.dp)
            .clickable { onImageClick() }
            .size(128.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Log.d("ProfileCamera",imageUri.toString())
            val imageBitmap = rememberImageBitmapFromUri(imageUri)
            if (imageBitmap != null) {
                Image(
                    painter = BitmapPainter(imageBitmap),
                    contentDescription = stringResource(R.string.icon_profile),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PermIdentity,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(R.string.icon_profile),
                )
            }
        }

        TextComponent(text = name, onTextChanged = onNameChange, label = R.string.name)
        ButtonComponent(onClick = onUploadClick, enable = true, buttonText = R.string.done)
        TextTermCondition(false)
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Preview("Dark Mode", device = Devices.PIXEL_3, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfilePreview() {

    EcommerceAppTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        ProfileScreen (
            uiState = ProfileUiState(
                userName = "Atifa"
            ),
            onNameChange = {},
            onUploadClick = {},
            onImageClick = {},
            snackBarMessageShown = {},
            snackBarHostState = snackBarHostState
        )
    }
}
