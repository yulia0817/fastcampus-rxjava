package com.maryang.fastrxjava.ui.repos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.base.BaseActivity
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.event.DataObserver
import kotlinx.android.synthetic.main.activity_github_repos.*


class GithubReposActivity : BaseActivity(), GithubReposPresenter.View {

    private val presenter: GithubReposPresenter by lazy {
        GithubReposPresenter(this)
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.maryang.fastrxjava.R.layout.activity_github_repos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = this.adapter

        refreshLayout.setOnRefreshListener { presenter.searchGithubRepos() }

        searchText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(text: Editable?) {
                presenter.searchGithubRepos(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        subscribeDataObserver()
    }

    private fun subscribeDataObserver() {
        DataObserver.observe()
            .filter { it is GithubRepo }
            .subscribe { repo ->
                adapter.items.find {
                    it.id == repo.id
                }?.apply {
                    star = star.not()
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }

    override fun showRepos(repos: List<GithubRepo>) {
        adapter.items = repos
    }
}
