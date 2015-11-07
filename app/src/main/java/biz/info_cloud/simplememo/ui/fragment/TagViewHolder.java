package biz.info_cloud.simplememo.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import biz.info_cloud.simplememo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class TagViewHolder {
    @Bind(R.id.tagflow_item_text)
    TextView tag;
    @Bind(R.id.tagflow_item_delete)
    ImageView delte;

    TagViewHolder(View view) {
        this(view, false);
    }

    TagViewHolder(View view, boolean enableDelete) {
        ButterKnife.bind(this, view);
        if (enableDelete) {
            this.delte.setVisibility(View.VISIBLE);
        }
    }
}
