package com.raf.core.presentation.components

import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.IconToggleButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun customButtonShapes() = ButtonShapes(
    shape = MaterialTheme.shapes.extraLarge,
    pressedShape = MaterialTheme.shapes.medium
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun customIconButtonShapes() = IconButtonShapes(
    shape = MaterialTheme.shapes.extraLarge,
    pressedShape = MaterialTheme.shapes.medium
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun customIconToggleButtonShapes() = IconToggleButtonShapes(
    shape = MaterialTheme.shapes.extraLarge,
    pressedShape = MaterialTheme.shapes.medium,
    checkedShape = MaterialTheme.shapes.medium
)