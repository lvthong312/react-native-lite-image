package com.liteimageview

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.Activity
class LiteImageViewView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

  /** TTL in seconds (default 1 hour). */
  var cacheTTL: Long = 3600

  private val prefs = context.getSharedPreferences("LiteImageViewCachePrefs", Context.MODE_PRIVATE)
  private val keyPrefix = "LiteImageViewCacheTS_"

  init {
    scaleType = ScaleType.CENTER_CROP
    adjustViewBounds = true
    clipToOutline = true
  }

  fun loadImage(uri: String?, resizeMode: String?) {
    if (uri.isNullOrEmpty()) {
      setImageDrawable(null)
      return
    }

    when (resizeMode) {
      "contain" -> scaleType = ScaleType.FIT_CENTER
      "stretch" -> scaleType = ScaleType.FIT_XY
      "center" -> scaleType = ScaleType.CENTER
      else -> scaleType = ScaleType.CENTER_CROP
    }

    val cacheKey = uri.hashCode().toString()
    val tsKey = keyPrefix + cacheKey
    val now = System.currentTimeMillis() / 1000
    val timestamp = prefs.getLong(tsKey, 0L)

    if (timestamp > 0 && now - timestamp < cacheTTL) {
      loadWithGlide(uri)
      return
    }

    clearFromCache()
    prefs.edit().putLong(tsKey, now).apply()
    loadWithGlide(uri)
  }

  private fun loadWithGlide(uri: String) {
    Glide.with(context)
      .load(uri)
      .apply(
        RequestOptions()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .dontAnimate()
      )
      .into(this)
  }

  private fun clearFromCache() {
    Thread {
      try {
        Glide.get(context.applicationContext).clearDiskCache()
      } catch (_: Exception) {}
    }.start()
  }

  fun clearTimestamps() {
    prefs.all.keys.filter { it.startsWith(keyPrefix) }
      .forEach { prefs.edit().remove(it).apply() }
  }
}
