package com.staygrateful.poi_test.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.staygrateful.poi_test.ui.theme.ColorContainerDark
import com.staygrateful.poi_test.ui.theme.ColorPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchInputField(
    hint: String,
    visibleSearchCancel: Boolean = false,
    onValueChange: (String) -> Unit = {},
    onFocusChanged: (FocusState) -> Unit = {},
    onCancel: () -> Unit = {},
    onSearch: (String) -> Unit = {},
    margin: PaddingValues = PaddingValues(),
    height: Dp = 40.dp,
    rounded: Dp = 11.dp,
) {

    val keyboard = LocalSoftwareKeyboardController.current
    var value by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(margin)
            .animateContentSize(tween(durationMillis = 350)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier
                .onFocusChanged(onFocusChanged)
                .weight(1f)
                .height(height)
                .border(1.dp, Color.LightGray.copy(0.5f), RoundedCornerShape(rounded)),
            value = value,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboard?.hide()
                    onSearch.invoke(value)
                }
            ),
            onValueChange = {
                value = it
                onValueChange.invoke(value)
            },
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.DecorationBox(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    value = value,
                    innerTextField = innerTextField,
                    visualTransformation = VisualTransformation.None,
                    placeholder = {
                        Text(
                            modifier = Modifier, text = hint,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            color = Color.Gray,
                            maxLines = 1
                        )
                    },
                    label = null,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            "Search",
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .size(20.dp),
                            tint = Color.Black
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = value.isNotEmpty(),
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                "Clear",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        value = ""
                                    }
                                    .padding(3.dp)
                                    .size(18.dp)
                                    .background(Color.Gray.copy(0.7f), CircleShape)
                                    .padding(2.dp),
                                tint = Color.White
                            )
                        }
                    },
                    prefix = null,
                    suffix = null,
                    supportingText = null,
                    shape = RoundedCornerShape(rounded),
                    singleLine = true,
                    enabled = true,
                    isError = false,
                    interactionSource = remember { MutableInteractionSource() },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ColorContainerDark,
                        unfocusedContainerColor = ColorContainerDark,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = ColorPrimary,
                    ),
                )
            }
        )
        AnimatedVisibility(
            visible = visibleSearchCancel,
        ) {
            CustomButton(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .height(35.dp),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(rounded),
                onClick = {
                    onCancel.invoke()
                }) {
                Text(text = "Cancel", color = ColorPrimary, fontSize = 13.sp)
            }
        }
    }
}