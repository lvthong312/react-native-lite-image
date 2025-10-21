package com.liteimageview

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

@ReactModule(name = LiteImageViewViewManager.NAME)
class LiteImageViewViewManager : SimpleViewManager<LiteImageViewView>() {

    override fun getName(): String = NAME

    override fun createViewInstance(context: ThemedReactContext): LiteImageViewView {
        return LiteImageViewView(context)
    }

    @ReactProp(name = "source")
    fun setSource(view: LiteImageViewView, value: ReadableMap?) {
        val uri = value?.getString("uri")
        view.loadImage(uri, null)
    }

    @ReactProp(name = "resizeMode")
    fun setResizeMode(view: LiteImageViewView, value: String?) {
        view.loadImage(view.drawable?.toString(), value) // or store uri in property
    }

    @ReactProp(name = "cacheTTL")
    fun setCacheTTL(view: LiteImageViewView, value: Double) {
        view.cacheTTL = value.toLong()
    }

    companion object {
        const val NAME = "LiteImageViewView"
    }
}
