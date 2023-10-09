package com.raees.steganographicapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import java.io.InputStream
import java.util.UUID
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EmbedTextScreen(navController: NavController, db: StegImageDatabase) {
    val viewModel = viewModel<EmbedTextScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EmbedTextScreenViewModel(db.dao) as T
            }
        }
    )

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val imageUri = rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    var imageBitmap: Bitmap? = null

    var message by rememberSaveable {
        mutableStateOf("")
    }
    var secretKey by rememberSaveable {
        mutableStateOf("")
    }

    val selectImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri.value = it
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview())
    {
        imageBitmap = it
    }

    if (imageUri.value != null) {
        imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri.value)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .scrollable(scrollState, orientation = Orientation.Vertical)
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.7f)
                .height(300.dp)
                .background(Color.LightGray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            if (imageBitmap != null){
                Image(
                    painter = rememberAsyncImagePainter(model = imageBitmap),
                    contentDescription = "",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        ElevatedButton(
            onClick = { selectImageLauncher.launch("image/*") },
            modifier = Modifier.padding(4.dp)
        ) {
            Text("Choose an image")
        }
        
        OutlinedButton(
            onClick = { takePictureLauncher.launch() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Take a picture")
        }

        TextField(
            value = message,
            onValueChange = { message = it},
            placeholder = {
                Text("Enter your Message")
            },
            label = {
                Text(text = "Message")
            },
            modifier = Modifier.padding(4.dp)
        )

        TextField(
            value = secretKey,
            onValueChange = { secretKey = it},
            placeholder = {
                Text("Enter a secure secret key")
            },
            label = {
                Text(text = "Secret Key")
            },
            modifier = Modifier.padding(4.dp)
        )

        Button(
            onClick = {
                if (imageBitmap != null) {
                    if (viewModel.addImage(
                            imageBitmap!!,
                            UUID.randomUUID().toString(),
                            message,
                            secretKey,
                            context
                        )
                    ) {
                        Toast.makeText(context, "Image added successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Home.route)
                    }
                    else {
                        Toast.makeText(context, "Failed to add image", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.padding(4.dp)
        ) {
            Text(text = "Embed Text")
        }
    }
}


//@Preview(showSystemUi = true)
//@Composable
//fun PreviewEmbedTextScreen() {
//    EmbedTextScreen()
//}