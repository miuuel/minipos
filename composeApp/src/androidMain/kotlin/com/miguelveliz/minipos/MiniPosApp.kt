package com.miguelveliz.minipos

import android.app.Application
import com.miguelveliz.minipos.di.initKoin

class MiniPosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}


