package com.tech.cybercars.ui.component.search_address_result_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tech.cybercars.R;
import com.tech.cybercars.adapter.search_address.SearchAddressResultAdapter;
import com.tech.cybercars.adapter.search_address.SearchAddressResultCallback;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;

import java.util.ArrayList;
import java.util.List;


public class SearchAddressResultView extends LinearLayout {
    private final List<ReverseGeocodingResponse> search_address_result;
    private SearchAddressResultAdapter adapter;
    private RecyclerView rcv;

    public SearchAddressResultView(@NonNull Context context) {
        this(context, null);
    }

    public SearchAddressResultView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SearchAddressResultView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        search_address_result = new ArrayList<>();
        Init(context);
    }

    private void Init(Context context) {
        inflate(context, R.layout.view_search_address_result, this);
        adapter = new SearchAddressResultAdapter(search_address_result);
    }

    public void SetOnItemClick(SearchAddressResultCallback callback) {
        if (adapter != null) {
            adapter.SetOnItemClick(callback);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        InitRCV();
    }

    private void InitRCV() {
        rcv = findViewById(R.id.rcv_search_address_result);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        rcv.setLayoutManager(layoutManager);
        rcv.setNestedScrollingEnabled(false);
        rcv.setAdapter(adapter);
    }

    public void SetOnScroll(OnScrollChangeListener callback){
        rcv.setOnScrollChangeListener(callback);
    }

    public List<ReverseGeocodingResponse> GetSearchAddressResultList() {
        return search_address_result;
    }

    public void NotifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}
