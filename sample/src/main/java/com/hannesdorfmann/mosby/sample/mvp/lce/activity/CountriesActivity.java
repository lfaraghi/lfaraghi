package com.hannesdorfmann.mosby.sample.mvp.lce.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.InjectView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceActivity;
import com.hannesdorfmann.mosby.sample.R;
import com.hannesdorfmann.mosby.sample.mvp.CountriesAdapter;
import com.hannesdorfmann.mosby.sample.mvp.CountriesErrorMessage;
import com.hannesdorfmann.mosby.sample.mvp.CountriesPresenter;
import com.hannesdorfmann.mosby.sample.mvp.lce.SimpleCountriesPresenter;
import com.hannesdorfmann.mosby.sample.mvp.CountriesView;
import com.hannesdorfmann.mosby.sample.mvp.model.Country;
import java.util.List;

public class CountriesActivity
    extends MvpLceActivity<SwipeRefreshLayout, List<Country>, CountriesView, CountriesPresenter>
    implements CountriesView, SwipeRefreshLayout.OnRefreshListener {

  @InjectView(R.id.recyclerView) RecyclerView recyclerView;

  CountriesAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.countries_list);

    // Setup contentView == SwipeRefreshView
    contentView.setOnRefreshListener(this);

    // Setup recycler view
    adapter = new CountriesAdapter(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    loadData(false);
  }

  public void loadData(boolean pullToRefresh) {
    presenter.loadCountries(pullToRefresh);
  }

  @Override protected void onErrorViewClicked() {
    loadData(false);
  }

  @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return CountriesErrorMessage.get(e, pullToRefresh, this);
  }

  @Override protected CountriesPresenter createPresenter() {
    return new SimpleCountriesPresenter();
  }

  @Override public void setData(List<Country> data) {
    adapter.setCountries(data);
    adapter.notifyDataSetChanged();
  }

  @Override public void onRefresh() {
    loadData(true);
  }

  @Override public void showContent() {
    super.showContent();
    contentView.setRefreshing(false);
  }

  @Override public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    contentView.setRefreshing(false);
  }
}
