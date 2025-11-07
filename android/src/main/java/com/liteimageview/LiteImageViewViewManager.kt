package com.liteimageview

import android.graphics.Color
import android.widget.Toast
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.LiteImageViewViewManagerInterface
import com.facebook.react.viewmanagers.LiteImageViewViewManagerDelegate

@ReactModule(name = LiteImageViewViewManager.NAME)
class LiteImageViewViewManager : SimpleViewManager<LiteImageViewView>(),
  LiteImageViewViewManagerInterface<LiteImageViewView> {
  private val mDelegate: ViewManagerDelegate<LiteImageViewView>

  init {
    mDelegate = LiteImageViewViewManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<LiteImageViewView>? {
    return mDelegate
  }

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): LiteImageViewView {
    return LiteImageViewView(context)
  }

  @ReactProp(name = "source")
  override fun setSource(view: LiteImageViewView, value: ReadableMap?) {
    val uri = value?.getString("uri")
    view.loadImage(uri, null, null)
  }


    @ReactProp(name = "resizeMode")
    override fun setResizeMode(view: LiteImageViewView, value: String?) {
        view.loadImage(view.drawable?.toString(),null, value) // or store uri in property
    }

    @ReactProp(name = "cacheTTL")
    override fun setCacheTTL(view: LiteImageViewView, value: Double) {
        view.cacheTTL = value.toLong()
    }

    companion object {
        const val NAME = "LiteImageViewView"
    }
}
