package biz.info_cloud.simplememo.ui.fragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.info_cloud.simplememo.R;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.ui.presenter.ContentListPresenter;
import biz.info_cloud.simplememo.ui.presenter.Presenter;
import biz.info_cloud.simplememo.util.StringUtil;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Observable;
import rx.Subscription;

public class ContentListFragment extends BaseFragment implements ContentListPresenter.MvpView {
    private ContentListAdapter contentListAdapter;

    @Inject
    ContentListPresenter contentListPresenter;

    @Bind(R.id.content_list)
    ListView contentList;

    // override/implements BaseFragment

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_content_list;
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    public void initialize() {
        getComponent().inject(this);
        contentListPresenter.setMvpView(this);
        contentListAdapter = new ContentListAdapter(getActivity());
        contentList.setAdapter(contentListAdapter);
    }

    @Override
    public Presenter getPresenter() {
        return contentListPresenter;
    }

    // MvpView interface

    @Override
    public void updateMemoList(List<Memo> memoList) {
        contentListAdapter.updateMemoList(memoList);
    }

    // ButterKnife event handler

    @OnItemClick(R.id.content_list)
    public void onContenListItemClicked(int position) {
        Memo memo = (Memo) contentListAdapter.getItem(position);
        contentListPresenter.onListItemClicked(memo);
    }

    static class ContentListAdapter extends BaseAdapter {
        private Activity activity;
        private List<Memo> memoList = new ArrayList<>();

        public ContentListAdapter(Activity activity) {
            this.activity = activity;
        }

        public void updateMemoList(List<Memo> memoList) {
            this.memoList.clear();
            this.memoList.addAll(memoList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return memoList.size();
        }

        @Override
        public Object getItem(int position) {
            return memoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            Memo memo = (Memo) getItem(position);
            return memo.getId().hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Memo memo = (Memo) getItem(position);

            View view = convertView;
            if (view == null) {
                view = this.activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
                view.setTag(new ItemViewHolder(view));
            }
            ItemViewHolder viewHolder = (ItemViewHolder) view.getTag();
            viewHolder.title.setText(memo.getTitle());
            viewHolder.content.setText(memo.getContent());

            viewHolder.tags.removeAllViews();
            if (memo.getTags() != null) {
                Observable.from(memo.getTags())
                        .filter(tag -> !StringUtil.isNullOrEmpty(tag.getName()))
                        .forEach(tag -> {
                            View tagView = this.activity.getLayoutInflater()
                                    .inflate(R.layout.tag_flow_item, viewHolder.tags, false);
                            TagViewHolder tagViewHolder = new TagViewHolder(tagView);
                            tagViewHolder.tag.setText(tag.getName());
                            viewHolder.tags.addView(tagView);
                        });
            }
            return view;
        }

        static class ItemViewHolder {
            @Bind(R.id.item_title)
            TextView title;
            @Bind(R.id.item_content)
            TextView content;
            @Bind(R.id.item_tags)
            FlowLayout tags;

            ItemViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }
}
