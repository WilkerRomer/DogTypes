package com.example.dogtypes.dogdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.dogtypes.R
import com.example.dogtypes.dogdetail.ui.theme.DogTypesTheme
import com.example.dogtypes.domain.Dog

@ExperimentalCoilApi
@Composable
fun MostProbableDogsDialog (
    mostProbableDogs: MutableList<Dog>,
    onShowMostProbableDogsDialogDismiss: () -> Unit,
    onItemClicked: (Dog) -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onShowMostProbableDogsDialogDismiss()
        },
        title = {
            Text(
                text = stringResource(id = R.string.other_probable_dogs),
                color = colorResource(id = R.color.text_black),
                fontSize = 29.sp,
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            MostProbableDogsList(mostProbableDogs) {
                onItemClicked(it)
                onShowMostProbableDogsDialogDismiss()
            }
        },
        buttons = {
            Row (
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {  }
                ) {
                   Text(stringResource(id = R.string.dismiss))
                }
            }
        }
    )
}

@ExperimentalCoilApi
@Composable
fun MostProbableDogsList(dogs: MutableList<Dog>, onItemClicked: (Dog) -> Unit) {
    Row(
        Modifier.height(250.dp)
    ) {
        LazyColumn(
            content = {
                items(dogs) {
                    MostProbableDogItem(dog = it, onItemClicked)
                }
            }
        )
    }
}
@ExperimentalCoilApi
@Composable
fun MostProbableDogItem(dog: Dog, onItemClicked: (Dog) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                enabled = true,
                onClick = { onItemClicked(dog) }
            ),
    ) {
        Text(
            dog.name,
            modifier = Modifier.padding(8.dp),
            color = colorResource(id = R.color.text_black),
        )
    }

}
@ExperimentalCoilApi
@Composable
@Preview(showBackground = true)
fun MostProbableDogsDialogsPreview() {
    DogTypesTheme {
        Surface {
            MostProbableDogsDialog(getFakeDogs(), {}) {}
        }
    }
}

fun getFakeDogs(): MutableList<Dog> {
    val dogList = mutableListOf<Dog>()
    dogList.add(
       Dog(
            1, 1, "Chihuahua", "Toy",
            "19", "20", "Brave", "12 - 10",
            "happy", "3.3", "2.2", false
        )
    )

    dogList.add(
        Dog(
            1, 1, "Chow chow", "Toy",
            "19", "20", "Brave", "12 - 10",
            "happy", "3.3", "2.2", true
        )
    )

    dogList.add(
        Dog(
            1, 1, "Pekines", "Toy",
            "19", "20", "Brave", "12 - 10",
            "happy", "3.3", "2.2", false
        )
    )
    return  dogList
}



