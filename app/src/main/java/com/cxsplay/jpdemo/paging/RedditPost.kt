package com.cxsplay.jpdemo.paging

data class RedditPost(
    val name: String,
    val title: String,
    val score: String,
    val author: String,
    val subreddit: String,
    val num_comments: Int,
    val created: Long,
    val thumbnail: String?,
    val url: String
) {
    var indexInRespone: Int = -1
}
