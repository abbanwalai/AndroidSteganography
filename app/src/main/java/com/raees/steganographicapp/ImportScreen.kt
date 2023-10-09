package com.raees.steganographicapp

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.util.UUID


@Composable
fun ImportScreen(navController: NavController) {
    val context = LocalContext.current
    val imageUri = rememberSaveable {
        mutableStateOf<Uri?>(null)
    }
    var imageBitmap: Bitmap? = null
    val selectImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        imageUri.value = it
    }

    if (imageUri.value != null) {
        imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri.value)
    }

    Column(
       Modifier.fillMaxSize(),
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       Box(
           modifier = Modifier
               .fillMaxWidth(0.7f)
               .height(300.dp)
               .background(Color.LightGray),
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
           onClick = {
               selectImageLauncher.launch("image/*")
           },
           modifier = Modifier.padding(vertical = 4.dp)
       ) {
           Text(text = "Choose an image")
       }

       Button(
           onClick = {
               if (imageBitmap != null) {
                   if (StegImageRepository.savePhotoToInternalStorage(
                           UUID.randomUUID().toString(),
                           imageBitmap,
                           context
                       )
                   ) {
                       Toast.makeText(context, "Image imported successfully", Toast.LENGTH_SHORT)
                           .show()
                       navController.navigate(Home.route)
                   } else {
                       Toast.makeText(context, "Failed to import Image", Toast.LENGTH_SHORT).show()
                   }
               }
               else {
                   Toast.makeText(context, "Choose an image first!", Toast.LENGTH_SHORT).show()
               }
           },
           modifier = Modifier.padding(vertical = 4.dp)
       ) {
           Text(text = "Import")
       }
   }
}


//@Preview(showSystemUi = true)
//@Composable
//fun PreviewImportScreen() {
//    ImportScreen(::savePhotoToInternalStorage)
//}