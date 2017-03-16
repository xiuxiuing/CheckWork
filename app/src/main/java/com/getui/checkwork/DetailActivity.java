package com.getui.checkwork;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.getui.checkwork.bean.RecordBean;
import com.getui.checkwork.utils.SystemUtils;

/**
 * Created by wang on 16/7/2.
 */
public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    private ProgressBar progressBar;
    private List<RecordBean> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        // mList = DBHelper.getInstance(this).queryTime();
        mList = new ArrayList<RecordBean>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setTitle("签到记录");

        mAdapter = new MyAdapter(mList);
        recyclerView.setAdapter(mAdapter);

        BmobQuery<RecordBean> query = new BmobQuery<RecordBean>();
        query.addWhereEqualTo("imei", SystemUtils.getImei(this));

        query.findObjects(new FindListener<RecordBean>() {
            @Override
            public void done(List<RecordBean> list, BmobException e) {
                if (e == null) {
                    System.out.println(list.size());
                    mList.clear();
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();

                }
            }
        });



    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private static final int ANIMATED_ITEMS_COUNT = 11;
        private boolean animateItems = false;
        private int lastAnimatedPosition = -1;
        private List<RecordBean> mList;

        public MyAdapter(List<RecordBean> list) {
            this.mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            runEnterAnimation(holder.itemView, position);
            RecordBean recordBean = mList.get(position);
            holder.tvRecord.setText(recordBean.getWeek() + " " + recordBean.getDate() + " " + recordBean.getTime());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvRecord;

            public ViewHolder(View v) {
                super(v);
                tvRecord = (TextView) v.findViewById(R.id.tv_record);
            }
        }

        private void runEnterAnimation(View view, int position) {
            if (position >= ANIMATED_ITEMS_COUNT - 1) {
                return;
            }

            if (position > lastAnimatedPosition) {
                lastAnimatedPosition = position;
                view.setTranslationY(getScreenHeight(DetailActivity.this));
                view.animate().translationY(0).setStartDelay(200 * position).setInterpolator(new DecelerateInterpolator(3.f)).setDuration(500)
                        .start();
            }
        }
    }

    public int getScreenHeight(Context c) {
        int screenHeight = 0;
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }
}
