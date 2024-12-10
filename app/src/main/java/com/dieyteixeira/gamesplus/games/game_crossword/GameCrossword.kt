package com.dieyteixeira.gamesplus.games.game_crossword

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val LocalDraggableItemInfo = compositionLocalOf { DraggableItemInfo() }

@Composable
fun GameCrossword(
    navigateClick: Boolean
) {

    GameScreen {
        GameViews()
    }

}

@Composable
fun GameScreen(
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DraggableItemInfo() }
    CompositionLocalProvider(
        LocalDraggableItemInfo provides state
    ) {
        Box(modifier = Modifier.fillMaxSize())
        {
            content()

            //here we will scale the draggable item when user start dragging
            if (state.isDragging) {
                var targetSize by remember {
                    mutableStateOf(IntSize.Zero)
                }
                Box(modifier = Modifier
                    .graphicsLayer {
                        val offset = (state.dragStartOffset + state.dragCurrentOffset)
                        scaleX = 1.3f
                        scaleY = 1.3f
                        alpha = if (targetSize == IntSize.Zero) 0f else .9f
                        translationX = offset.x.minus(targetSize.width / 2)
                        translationY = offset.y.minus(targetSize.height)
                    }
                    .onGloballyPositioned {
                        targetSize = it.size
                    }
                ) {
                    state.draggableComposable?.invoke()
                }
            }
        }
    }
}

@Composable
fun <T> DraggableView(
    modifier: Modifier = Modifier,
    dataToDrop: T ,
    content: @Composable (() -> Unit)
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    //access the current state of our DraggableInfo
    val currentState = LocalDraggableItemInfo.current

    Box(modifier = modifier
        .onGloballyPositioned { layoutCoordinates -> //Retrieves layout information (position and size) of a composable.
            currentPosition = layoutCoordinates.localToWindow(
                Offset.Zero
            )
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = { startOffset ->
                currentState.dataToDrop = dataToDrop
                currentState.isDragging = true
                currentState.dragStartOffset = currentPosition + startOffset
                currentState.draggableComposable = content
            }, onDrag = { change, dragAmount ->
                change.consume()
                currentState.dragCurrentOffset += Offset(dragAmount.x, dragAmount.y)
            }, onDragEnd = {
                currentState.isDragging = false
                currentState.dragCurrentOffset = Offset.Zero
            }, onDragCancel = {
                currentState.dragCurrentOffset = Offset.Zero
                currentState.isDragging = false
            })
        }) {
        content()
    }
}

@Composable
fun <T> DropTarget(
    modifier: Modifier,
    content: @Composable() (BoxScope.(isInBound: Boolean, data: T?) -> Unit)
) {

    val dragInfo = LocalDraggableItemInfo.current
    val dragPosition = dragInfo.dragStartOffset
    val dragOffset = dragInfo.dragCurrentOffset
    var isCurrentDropTarget by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier.onGloballyPositioned { layoutCoordinates ->  //Retrieves layout information (position and size) of a composable.
        layoutCoordinates.boundsInWindow().let { rect ->
            isCurrentDropTarget = rect.contains(dragPosition + dragOffset)
        }
    }) {
        val data =
            if (isCurrentDropTarget && !dragInfo.isDragging) dragInfo.dataToDrop as T? else null
        content(isCurrentDropTarget, data)
    }
}

internal class DraggableItemInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragStartOffset by mutableStateOf(Offset.Zero)
    var dragCurrentOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
}


@Composable
fun GameViews(
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val boxSize = Dp(screenWidth / 6f)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Word Game",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Magenta.copy(0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        val gridData: Array<Array<DataItem>> = arrayOf(
            arrayOf(DataItem("0"), DataItem("W", "W"), DataItem("0"), DataItem("A", "")),
            arrayOf(DataItem("G", "G"), DataItem("#", "O"), DataItem("A", "A"), DataItem("#", "T")),
            arrayOf(DataItem("0"), DataItem("N", "N"), DataItem("0"), DataItem("M", ""))
        )

        //show target layout
        GridViews(gridData, boxSize)

        val dragItems = listOf(
            DataItem("A"), DataItem("O"), DataItem("T")
        )

        //show draggable views
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dragItems.forEach { person ->
                DraggableView(
                    dataToDrop = person
                ) {
                    Box(
                        modifier = Modifier
                            .size(boxSize)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.Green.copy(alpha = 0.5f), RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = person.name!!,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GridViews(data: Array<Array<DataItem>>, boxSize: Dp) {
    var dataList by remember {
        mutableStateOf(data)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //loop in columns
        for (row in dataList) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //loop in rows
                for (item in row) {
                    when (item.name) {
                        "#" -> {
                            //Show drop target if applicable
                            DropTarget<DataItem>(
                                modifier = Modifier
                                    .size(boxSize)
                            ) { isInBound, personItem ->

                                val color = if (isInBound)
                                    Color.Red.copy(0.5f)
                                else
                                    Color.Gray.copy(0.2f)

                                personItem?.let {
                                    if (isInBound) {
                                        val result =
                                            findElementIndex(
                                                data,
                                                DataItem(name = "#", id = personItem.name)
                                            ) ?: Pair(0, 0)

                                        //if we didn't find the element then don't show the box at 0,0
                                        if (result.first!=0 && result.second!=0){
                                            //we only update dataList if draggable box matches the drop target
                                            dataList = dataList.mapIndexed { rowIndex, row ->
                                                row.mapIndexed { colIndex, element ->
                                                    if (rowIndex == result.first && colIndex == result.second) {
                                                        DataItem(
                                                            name = personItem.name,
                                                            id = personItem.name,
                                                            Color.Blue.copy(alpha = 0.5f)
                                                        )
                                                    } else {
                                                        element
                                                    }
                                                }.toTypedArray()
                                            }.toTypedArray()
                                        }

                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            color,
                                            RoundedCornerShape(15.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (personItem != null) {
                                        Text(
                                            text = personItem.name!!,
                                            style = MaterialTheme.typography.headlineLarge,
                                            color = Color.White
                                        )
                                    }

                                }
                            }
                        }

                        "0" -> {
                            //We have to keep this place empty
                            Box(
                                modifier = Modifier
                                    .size(boxSize),
                                contentAlignment = Alignment.Center
                            ) {

                            }
                        }

                        else -> {
                            //Means We have to show the box
                            Box(
                                modifier = Modifier
                                    .size(boxSize)
                                    .background(
                                        item.color,
                                        RoundedCornerShape(15.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.name!!,
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

// Return Pair of indices of the target element from the passed matrix
fun <T> findElementIndex(matrix: Array<Array<T>>, target: T): Pair<Int, Int>? {
    //loop in rows
    for (row in matrix.indices) {
        //loop in columns
        for (column in matrix[row].indices) {
            if (matrix[row][column] == target) {
                return Pair(row, column)  // Found the element, return its indices as a pair
            }
        }
    }
    return null  // Element not found
}

data class DataItem(
    val name: String? = "",
    val id: String? = "",
    val color: Color = Color.Gray
)