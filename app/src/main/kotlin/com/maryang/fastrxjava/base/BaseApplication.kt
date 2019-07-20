package com.maryang.fastrxjava.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.maryang.fastrxjava.event.EventBus
import com.maryang.fastrxjava.event.LogoutEvent
import com.maryang.fastrxjava.util.ErrorHandler
import io.reactivex.plugins.RxJavaPlugins
import org.jetbrains.anko.toast

class BaseApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context
        const val TAG = "FastRxJava2"
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Stetho.initializeWithDefaults(this)
        setErrorHanlder()
    }

    private fun setErrorHanlder() {
        // onError 가 없거나, onError에서 또 Exception이 나면 오는애
        RxJavaPlugins.setErrorHandler {
            ErrorHandler.globalHandle(it)
        }

        EventBus.observe().subscribe {
            when (it) {
                is LogoutEvent ->
                    toast("에러났음")
            }
        }
    }
}
