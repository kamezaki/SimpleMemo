package biz.info_cloud.simplememo.ui.fragment;

import android.view.View;
import android.widget.TextView;

import biz.info_cloud.simplememo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class TagViewHolder {
    @Bind(R.id.tagflow_item)
    TextView tag;

    TagViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
