package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.staygrateful.poi_test.R
import com.staygrateful.poi_test.data.models.Coordinates
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import com.staygrateful.poi_test.ui.theme.ColorDividerDark
import com.staygrateful.poi_test.ui.theme.ColorPrimary

@Composable
fun BoxScope.MapNav(viewModels: HomeViewModel) {

    Column(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(end = 10.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .width(45.dp)
                .height(100.dp)
                .shadow(10.dp, RoundedCornerShape(7.dp), true)
                .background(Color.White, RoundedCornerShape(7.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                "My Location",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                        viewModels.focusMapsCamera(Coordinates.fromGeo(viewModels.currentGeoLocation))
                    }
                    .padding(12.dp),
                tint = ColorPrimary
            )
            Spacer(
                modifier = Modifier
                    .height(0.7.dp)
                    .fillMaxWidth()
                    .background(ColorDividerDark),
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                "Pin Location",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable(viewModels.findAreaLocation.visible) {
                        viewModels.focusMapsCamera(viewModels.findAreaLocation.copy())
                    }
                    .padding(12.dp),
                tint = if (viewModels.findAreaLocation.visible) ColorPrimary else Color.Gray
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_compass),
            "North",
            modifier = Modifier
                .rotate(viewModels.mapOrientation.value)
                .size(35.dp),
        )
    }
}