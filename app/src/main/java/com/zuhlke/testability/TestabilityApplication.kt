package com.zuhlke.testability

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat

class TestabilityApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        EmojiCompat.init(BundledEmojiCompatConfig(applicationContext))
    }
}