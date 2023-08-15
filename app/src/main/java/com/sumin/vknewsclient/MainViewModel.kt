package com.sumin.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sumin.vknewsclient.domain.FeedPost
import com.sumin.vknewsclient.domain.StatisticItem
import com.sumin.vknewsclient.domain.StatisticType
import com.sumin.vknewsclient.ui.theme.NavigationItem
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(100) {
            add(
                FeedPost(
                    id = it,
                    statistics = listOf(
                        StatisticItem(StatisticType.VIEWS, Random.nextInt(0, 1000)),
                        StatisticItem(StatisticType.SHARES, Random.nextInt(0, 1000)),
                        StatisticItem(StatisticType.COMMENTS, Random.nextInt(0, 1000)),
                        StatisticItem(StatisticType.LIKES,  Random.nextInt(0, 1000))
                    )
                )
            )
        }
    }

    private val _feedPosts = MutableLiveData<List<FeedPost>>(initialList)
    val feedPosts: LiveData<List<FeedPost>> = _feedPosts

    private val _selectedNavItem = MutableLiveData<NavigationItem>(NavigationItem.Home)
    val selectedNavItem: LiveData<NavigationItem> = _selectedNavItem

    fun selectNavItem(item: NavigationItem) {
        _selectedNavItem.value = item
    }

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val modifiedFeedPosts = _feedPosts.value?.toMutableList() ?: mutableListOf()

        val oldStatistics = feedPost.statistics
        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type) {
                    oldItem.copy(count = item.count + 1)
                } else {
                    oldItem
                }
            }
        }

        modifiedFeedPosts.replaceAll {
            if (it.id == feedPost.id) {
                it.copy(statistics = newStatistics)
            } else {
                it
            }
        }

        _feedPosts.value = modifiedFeedPosts
    }

    fun remove(feedPost: FeedPost) {
        val modifiedFeedPosts = _feedPosts.value?.toMutableList() ?: mutableListOf()
        modifiedFeedPosts.remove(feedPost)
        _feedPosts.value = modifiedFeedPosts
    }
}