package id.ac.polinema.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import id.ac.polinema.musicplayer.R;
import id.ac.polinema.musicplayer.adapters.MainPagerAdapter;
import id.ac.polinema.musicplayer.adapters.SongsAdapter;
import id.ac.polinema.musicplayer.common.OnItemClickListener;
import id.ac.polinema.musicplayer.models.MainModel;
import id.ac.polinema.musicplayer.models.Resource;
import id.ac.polinema.musicplayer.models.Track;
import id.ac.polinema.musicplayer.viewmodels.TopTracksViewModel;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private static final String TAG = "MainActivity";
    TopTracksViewModel viewModel;
    RecyclerView mRecyclerview;
    SongsAdapter mSongsAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ViewPager mViewPager;
    MainPagerAdapter mAdapter;
    TabLayout mTabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_list);

        mTabLayout = findViewById(R.id.tl_main);


        mSongsAdapter = new SongsAdapter(this);

        mRecyclerview.setHasFixedSize(false);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
        mRecyclerview.setAdapter(mSongsAdapter);

        initializeFragments();
        fetchData();

    }

    private void fetchData() {
        viewModel = new ViewModelProvider(MainActivity.this).get(TopTracksViewModel.class);
        viewModel.init();
        viewModel.getTopTracksRepository().observe(this, new Observer<Resource<MainModel>>() {
            @Override
            public void onChanged(Resource<MainModel> mainModelResource) {
                if (mainModelResource !=null){
                    switch (mainModelResource.status){
                        case ERROR:
                            mSwipeRefreshLayout.setRefreshing(false);
                            break;
                        case LOADING:
                            mSwipeRefreshLayout.setRefreshing(true);
                            break;
                        case SUCCESS:
                            if (mainModelResource.data !=null){
                                Timber.tag(TAG).d("mainModelResource%s", mainModelResource.data);
                                mSongsAdapter.setDataList(mainModelResource.data.getTracks().getTrack());
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            break;
                    }
                }
            }
        });
    }


    @Override
    public void onClick(Track track) {
        Intent intent = new Intent(MainActivity.this, SelectedSong.class);
        intent.putExtra("Data",new Gson().toJson(track));
        startActivity(intent);
    }

    private void initializeFragments() {
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
