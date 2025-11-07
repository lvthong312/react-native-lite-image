package com.liteimageview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView.ScaleType
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class LiteImageViewView : AppCompatImageView {
  constructor(context: Context?) : super(context!!)
  constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context!!,
    attrs,
    defStyleAttr
  )

  /** TTL in seconds (default 1 hour). */
  var cacheTTL: Long = 3600

  private val prefs = context.getSharedPreferences("LiteImageViewCachePrefs", Context.MODE_PRIVATE)
  private val keyPrefix = "LiteImageViewCacheTS_"

  init {
    scaleType = ScaleType.CENTER_CROP
    adjustViewBounds = true
    clipToOutline = true
  }

  /**
   * 🗺️ Phân giải URI/Tên resource của React Native thành URI hoặc Resource ID hợp lệ của Android.
   * @param uri URI nhận được từ JS (có thể là "src_...", "asset:/...", hoặc URL HTTP/S).
   * @return Pair<String? (URI cho Glide), Int? (Android Resource ID)>. Chỉ có một giá trị là non-null.
   */
  private fun getUriOrResourceId(uri: String?): Pair<String?, Int?> {
    if (uri.isNullOrEmpty()) {
      return Pair(null, null)
    }

    // 1. Xử lý Tên Resource (src_...)
    if (uri.startsWith("src_")) {
      val resId = context.resources.getIdentifier(
        uri,
        "drawable", // Giả định tài nguyên ảnh nằm trong drawable
        context.packageName
      )
      if (resId != 0) {
        return Pair(null, resId) // Trả về Resource ID
      }
    }

    // 2. Xử lý Asset URI (asset:/...)
    if (uri.startsWith("asset:/")) {
      // Chuyển đổi thành URI mà Glide/Android Asset Manager hiểu
      val assetPath = uri.removePrefix("asset:/")
      val glideUri = "file:///android_asset/$assetPath"
      return Pair(glideUri, null) // Trả về URI
    }

    // 3. Xử lý URL từ xa (HTTP/HTTPS) hoặc URI không được nhận dạng
    return Pair(uri, null) // Trả về URI gốc
  }

  fun loadImage(uri: String?, resourceId: Int?, resizeMode: String?) {

    // Áp dụng resizeMode trước
    when (resizeMode) {
      "contain" -> scaleType = ScaleType.FIT_CENTER
      "stretch" -> scaleType = ScaleType.FIT_XY
      "center" -> scaleType = ScaleType.CENTER
      else -> scaleType = ScaleType.CENTER_CROP
    }

    // 1. Ưu tiên Resource ID được truyền trực tiếp (R.drawable.xxx)
    if (resourceId != null) {
      loadWithGlide(null, resourceId)
      return
    }

    // 2. Phân giải URI/Tên resource từ React Native
    val (resolvedUri, resId) = getUriOrResourceId(uri)

    // Nếu tìm thấy Android Resource ID (từ src_...)
    if (resId != null) {
      loadWithGlide(null, resId)
      return
    }

    // Nếu URI vẫn null sau khi resolve hoặc URI gốc là null
    if (resolvedUri.isNullOrEmpty()) {
      setImageDrawable(null)
      return
    }

    // 3. Xử lý cache TTL cho URI (bao gồm cả URL từ xa và URI asset đã được resolve)
    val cacheKey = resolvedUri.hashCode().toString()
    val tsKey = keyPrefix + cacheKey
    val now = System.currentTimeMillis() / 1000
    val timestamp = prefs.getLong(tsKey, 0L)

    if (timestamp > 0 && now - timestamp < cacheTTL) {
      loadWithGlide(resolvedUri, null)
      return
    }

    clearFromCache()
    prefs.edit().putLong(tsKey, now).apply()
    loadWithGlide(resolvedUri, null)
  }

  // Các hàm còn lại không đổi

  private fun loadWithGlide(uri: String? = null, resId: Int?) {
    if(resId!= null) {
      Glide.with(context)
        .load(resId)
        .apply(
          RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
        )
        .into(this)
    }
    else if(!uri.isNullOrEmpty()) {
      Glide.with(context)
        .load(uri)
        .apply(
          RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
        )
        .into(this)
    }
    else {
      setImageDrawable(null)
    }
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
