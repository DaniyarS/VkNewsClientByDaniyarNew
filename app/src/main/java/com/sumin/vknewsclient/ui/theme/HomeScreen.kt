package com.sumin.vknewsclient.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sumin.vknewsclient.MainViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, paddingValues: PaddingValues) {
    val models = viewModel.feedPosts.observeAsState(listOf())

    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 72.dp,
            start = 8.dp,
            end = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(models.value, key = { it.id }) { model ->
            val dismissState = rememberDismissState()

            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                viewModel.remove(model)
            }

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState, background = { },
                directions = setOf(DismissDirection.EndToStart)
            ) {
                PostCard(
                    modifier = Modifier.padding(8.dp),
                    feedPost = model,
                    onViewClickListener = { viewModel.updateCount(model, it) },
                    onShareClickListener = { viewModel.updateCount(model, it) },
                    onLikeClickListener = { viewModel.updateCount(model, it) },
                    onCommentClickListener = { viewModel.updateCount(model, it) }
                )
            }
        }
    }
}