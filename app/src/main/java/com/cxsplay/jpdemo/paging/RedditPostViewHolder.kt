package com.cxsplay.jpdemo.paging

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cxsplay.jpdemo.GlideRequests
import com.cxsplay.jpdemo.R

/**
 * Created by chuxiaoshan on 2019/11/20 14:58.
 *
 */
class RedditPostViewHolder(view: View, private val glide: GlideRequests) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.title)
    private val subtitle: TextView = view.findViewById(R.id.subtitle)
    private val score: TextView = view.findViewById(R.id.score)
    private val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    private var post: RedditPost? = null

    init {
        view.setOnClickListener {
            post?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(post: RedditPost?) {
        this.post = post
        title.text = post?.title ?: "loading"
        subtitle.text = "Submitted by ${post?.author ?: "unknow"}"
        score.text = "${post?.score ?: 0}"
        if (post?.thumbnail?.startsWith("http") == true) {
            thumbnail.visibility = View.VISIBLE
            glide.load(post.thumbnail)
                .centerCrop()
                .placeholder(R.drawable.ic_insert_photo_black_48dp)
                .into(thumbnail)
        } else {
            thumbnail.visibility = View.GONE
            glide.clear(thumbnail)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): RedditPostViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.reddit_post_item, parent, false)
            return RedditPostViewHolder(view, glide)
        }
    }

    fun updateScore(item: RedditPost?) {
        post = item
        score.text = "${item?.score ?: 0}"
    }
}