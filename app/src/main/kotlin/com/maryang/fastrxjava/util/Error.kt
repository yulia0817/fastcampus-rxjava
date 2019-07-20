package com.maryang.fastrxjava.util

import com.maryang.fastrxjava.observer.DefaultSingleObserver
import io.reactivex.Single

object Error {

    fun error() {
        Single.error<Boolean>(IllegalStateException())
            .onErrorResumeNext {
                Single.just(true)
            }
            .onErrorReturn {
                true
            }
            .subscribe(object : DefaultSingleObserver<Boolean>() {
                override fun onSuccess(t: Boolean) {

                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    // 에러를 받는다
                }
            })

        Single.error<Unit>(IllegalStateException())
            .subscribe({

            }, {
                ErrorHandler.globalHandle(it)
                // 에러를 받는다
            })
    }
}
