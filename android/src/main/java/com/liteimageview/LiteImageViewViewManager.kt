package com.liteimageview

import android.graphics.Color
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

  @ReactProp(name = "color")
  override fun setColor(view: LiteImageViewView?, color: String?) {
    view?.setBackgroundColor(Color.parseColor(color))
  }

  companion object {
    const val NAME = "LiteImageViewView"
  }
}
