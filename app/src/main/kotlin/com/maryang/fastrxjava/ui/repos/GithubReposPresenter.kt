package com.maryang.fastrxjava.ui.repos

import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class GithubReposPresenter(
    private val view: View,
    private val repository: GithubRepository = GithubRepository()
) {
    private val searchSubject = PublishSubject.create<Pair<String, Boolean>>()
    private var searchText = ""

    init {
        subscribeSearchSubject()
    }

    fun searchGithubRepos(search: String) {
        searchSubject.onNext(search to true)
    }

    fun searchGithubRepos() {
        searchSubject.onNext(searchText to false)
    }

    private fun subscribeSearchSubject() {
        searchSubject
            .debounce(400, TimeUnit.MILLISECONDS, Schedulers.io())
            .doOnNext { searchText = it.first }
            .map { it.second }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { if (it) view.showLoading() }
            .observeOn(Schedulers.io())
            .flatMapSingle {
                repository.searchGithubRepos(searchText)
                    .flatMap {
                        Completable.merge(
                            it.map { repo ->
                                repository.checkStar(repo.owner.userName, repo.name)
                                    .doOnComplete { repo.star = true }
                                    .onErrorComplete()
                            }
                        ).toSingleDefault(it)
                    }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.hideLoading()
                view.showRepos(it)
            }, {
                it.printStackTrace()
                view.hideLoading()
            })
    }

    fun searchSubject(): Observable<List<GithubRepo>> {
        return searchSubject
            .debounce(400, TimeUnit.MILLISECONDS, Schedulers.io())
            .doOnNext {
                searchText = it.first
            }
            .map { it.second }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (it) view.showLoading()
            }
            .observeOn(Schedulers.io())
            .flatMapSingle {
                repository.searchGithubRepos(searchText)
                    .flatMap {
                        Completable.merge(
                            it.map { repo ->
                                repository.checkStar(repo.owner.userName, repo.name)
                                    .doOnComplete { repo.star = true }
                                    .onErrorComplete()
                            }
                        ).toSingleDefault(it)
                    }
            }
    }

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showRepos(repos: List<GithubRepo>)
    }
}
