package com.example.compose101

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose101.ui.theme.Compose101Theme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose101Theme {
                Scaffold {
                    Column {
                        var text by remember { mutableStateOf("") }
                        val focusManager = LocalFocusManager.current
                        CatSearchBar(
                            text,
                            onTextChange = {
                                text = it
                            },
                            onDeleteClick = {
                                focusManager.clearFocus()
                                text = ""
                            },
                        )
                        Divider(
                            Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                        )
                        CatsView(
                            catModelList = cats,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            filter = text
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CatSearchBar(
    text: String = "",
    onTextChange: (String) -> Unit,
    onDeleteClick: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                backgroundColor = Color(0xFFD1D1D1),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Search cat by name") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            ImageVector.vectorResource(id = R.drawable.ic_delete),
            null,
            colorFilter = ColorFilter.tint(Color.Red.copy(alpha = 0.5f)),
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    onDeleteClick()
                }
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun PreviewComposeSearchBar() {
    CatSearchBar(text = "", {})
}

@Composable
fun CatsView(modifier: Modifier = Modifier, filter: String = "", catModelList: List<CatModel>) {
    val filteredCats = if (filter.isNotEmpty()) {
        catModelList.filter {
            it.catName.lowercase(Locale.getDefault())
                .startsWith(filter.lowercase(Locale.getDefault()))
        }
    } else {
        catModelList
    }
    if (filteredCats.isNotEmpty()) {
        LazyColumn(modifier = modifier) {
            filteredCats.forEachIndexed { index, catModel ->
                val padding = if (index == catModelList.size - 1) {
                    PaddingValues(all = 16.dp)
                } else {
                    PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)
                }
                item {
                    CatCard(catModel = catModel, padding = padding)
                }
            }
        }
    } else {
        Column(modifier = modifier) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "No Cats Found", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun PreviewCatsView() {
    CatsView(catModelList = cats)
}

@Composable
fun CatCard(catModel: CatModel, padding: PaddingValues = PaddingValues()) {
    Surface(
        color = Color(0xFFADD8D6),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .aspectRatio(2.5f)
            .padding(padding)
    ) {
        Row {
            Image(
                painter = painterResource(id = catModel.catPictureResourceResId),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f, true)
                    .padding(16.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Divider(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Cat Name: ${catModel.catName}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Cat Age: ${catModel.catAge}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCatCard() {
    CatCard(
        catModel = CatModel(
            "Putham",
            "7 Month",
            R.drawable.cat_cireng
        )
    )
}

data class CatModel(
    val catName: String,
    val catAge: String,
    val catPictureResourceResId: Int
)

val cats = listOf(
    CatModel(
        "Black",
        "1 Year 2 Month",
        R.drawable.cat_black
    ),
    CatModel(
        "Cici",
        "1 Year 5 Month",
        R.drawable.cat_cici
    ),
    CatModel(
        "Cireng",
        "8 Month",
        R.drawable.cat_cireng
    ),
    CatModel(
        "Micky",
        "1 Year 3 Month",
        R.drawable.cat_micky
    ),
    CatModel(
        "Milo",
        "1 Year",
        R.drawable.cat_milo
    ),
    CatModel(
        "Orange",
        "1 Year 1 Month",
        R.drawable.cat_orange
    ),
    CatModel(
        "Purr",
        "8 Month",
        R.drawable.cat_purr
    )
)