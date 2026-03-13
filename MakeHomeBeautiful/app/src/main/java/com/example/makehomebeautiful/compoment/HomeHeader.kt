package com.example.makehomebeautiful.compoment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.makehomebeautiful.compoment.header.HeaderBookmark
import com.example.makehomebeautiful.compoment.header.HeaderHome
import com.example.makehomebeautiful.compoment.header.HeaderNotification
import com.example.makehomebeautiful.compoment.header.HeaderProfile

@Composable
fun HomeHeader(route: String?) {

    when (route) {

        "home" -> {
            HeaderHome()
        }
        "bookmark" -> {
            HeaderBookmark()
        }
        "notification" -> {
            HeaderNotification()
        }
        "profile" -> {
            HeaderProfile()
        }
    }
}