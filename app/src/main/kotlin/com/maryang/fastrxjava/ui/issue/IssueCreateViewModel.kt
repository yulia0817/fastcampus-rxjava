package com.maryang.fastrxjava.ui.issue

import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.android.schedulers.AndroidSchedulers

class IssueCreateViewModel {
    private val repository = GithubRepository()

    fun create(repo: GithubRepo, title: String, body: String) =
        repository.createIssue(repo.owner.userName, repo.name, title, body)
            .observeOn(AndroidSchedulers.mainThread())
}
