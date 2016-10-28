package ch.hsr_heroes.gadgeothek;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseListActivity extends BaseActivity {
    protected RecyclerView recyclerView;
    protected View emptyListPlaceholder;
    protected TextView emptyListText;
    @Override
    protected void onCreateMainContent(ViewGroup contentView) {
        LayoutInflater.from(this).inflate(R.layout.activity_base_list, contentView, true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyListPlaceholder = findViewById(R.id.empty_list_placeholder);
        emptyListText = (TextView) findViewById(R.id.empty_list_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    protected void clearEmptyMessage() {
        emptyListPlaceholder.setVisibility(View.GONE);
    }

    protected void setEmptyMessage(String msg) {
        emptyListText.setText(msg);
        emptyListPlaceholder.setVisibility(View.VISIBLE);
    }

    abstract void refreshList();

}
