package com.maryang.fastrxjava.util

import android.util.Log
import android.widget.TextView
import com.maryang.fastrxjava.base.BaseApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


object Memory {

    // Observer 객체가 TextView 객체를 들고있다
    // Activity 또는 View 또는 Fragment 등등의 View가 TextView를 들고있다
    // Activity가 종료되었다. 원래대로라면 TextView도 같이 종료.
    // 그런데 여기서는 Observer가 TextView를 들고있고, TextView를 바라보는 Activity도 안죽는다
    fun leakObservable(text: TextView) {
        Observable
            .interval(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { x ->
                Log.d(BaseApplication.TAG, "leakObservable $x")
                text.text = x.toString()
            }
    }
}
