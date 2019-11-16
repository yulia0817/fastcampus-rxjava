package com.maryang.fastrxjava.ui

import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.data.source.DefaultCallback
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response

class GithubReposViewModel {
    private val repository = GithubRepository()

    fun getGithubRepos () : Single<List<GithubRepo>> {
        return repository.getGithubRepos()
            .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
    }
}
