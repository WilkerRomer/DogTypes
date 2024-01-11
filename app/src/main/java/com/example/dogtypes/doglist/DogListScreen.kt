package com.example.dogtypes.doglist

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.dogtypes.R
import com.example.dogtypes.api.ApiResponseStatus
import com.example.dogtypes.composables.BackNavigationIcon
import com.example.dogtypes.composables.ErrorDialog
import com.example.dogtypes.composables.LoadingWheel
import com.example.dogtypes.domain.Dog

private const val GRID_SPAN_COUNT = 3

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun DogListScreen(
    onNavigationIconClick: () -> Unit,
    onDogClicked: (Dog) -> Unit,
    viewModel: DogListViewModel = hiltViewModel()
) {
    val status = viewModel.status.value
    val dogList = viewModel.dogList.value
    Scaffold(
        topBar = {DogListScreenTopBar(onNavigationIconClick)}
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_SPAN_COUNT),
            content = {
                items(dogList) {
                    DogGridItem(dog = it, onDogClicked)
                }
            }
        )
    }

    if (status is ApiResponseStatus.Loading<*>) {
        LoadingWheel()
    } else if (status is ApiResponseStatus.Error<*>){
        ErrorDialog(status.messageId) { viewModel.resetApiResponseStatus() }
    }
}

@ExperimentalMaterialApi
@Composable
fun DogListScreenTopBar(
    onClick: () -> Unit,
) {
    TopAppBar(
        title = {Text(stringResource(id = R.string.my_dog_collection))},
        backgroundColor = Color.White,
        contentColor = Color.Black,
        navigationIcon = {BackNavigationIcon(onClick)}

    )
}



@ExperimentalMaterialApi
@Composable
fun DogGridItem(dog: Dog, onDogClicked: (Dog) -> Unit ){
    if (dog.inCollection){
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            onClick = { onDogClicked(dog) },
            shape = RoundedCornerShape(4.dp)
        ) {
            Image(
                painter = rememberImagePainter(dog.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.White)
                    .semantics { testTag = "dog-${dog.name}" }
            )
        }
    } else {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            color = Color.Red,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = dog.index.toString(),
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                textAlign = TextAlign.Center,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
