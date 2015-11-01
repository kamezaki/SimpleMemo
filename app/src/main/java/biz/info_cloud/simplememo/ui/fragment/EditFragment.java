package biz.info_cloud.simplememo.ui.fragment;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apmem.tools.layouts.FlowLayout;

import javax.inject.Inject;

import biz.info_cloud.simplememo.R;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.ui.presenter.EditPresenter;
import biz.info_cloud.simplememo.ui.presenter.Presenter;
import biz.info_cloud.simplememo.util.StringUtil;
import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

public class EditFragment extends BaseFragment implements EditPresenter.MvpView {
    public static final String BUNDLE_MEMO_ID = EditFragment.class.getCanonicalName() + ".BUNDLE_MEMO_ID";

    private Memo memo;

    @Inject
    EditPresenter editPresenter;

    @Bind(R.id.edit_title) EditText editTitle;
    @Bind(R.id.edit_content) EditText editContent;
    @Bind(R.id.save_button) Button btnSave;
    @Bind(R.id.cancel_button) Button btnCancel;
    @Bind(R.id.tags_layout)
    FlowLayout tagsLayout;

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_edit_memo;
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    public void initialize() {
        getComponent().inject(this);
        editPresenter.setMvpView(this);
        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleSaveButton(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnSave.setEnabled(editTitle.length() > 0);
        if (getArguments() != null) {
            String menuId = getArguments().getString(BUNDLE_MEMO_ID);
            if (!StringUtil.isNullOrEmpty(menuId)) {
                editPresenter.findMemo(menuId);
            }
        }
    }

    @Override
    public Presenter getPresenter() {
        return editPresenter;
    }

    // ButterKnife event handler

    @OnClick(R.id.cancel_button)
    void onCancel() {
        editPresenter.cancel();
    }

    @OnClick(R.id.save_button)
    void onSave() {
        if (this.memo == null) {
            this.memo = new Memo(editTitle.getText().toString(), editContent.getText().toString());
        } else {
            this.memo.setTitle(editTitle.getText().toString());
            this.memo.setContent(editContent.getText().toString());
        }
        editPresenter.update(memo);
    }

    // fragment intrinsic functions

    private void handleSaveButton(boolean enable) {
        btnSave.setEnabled(enable);
    }

    private void showTagsView(@NonNull Memo memo) {
        // remove tags view except tag editor
        this.tagsLayout.removeViews(0, this.tagsLayout.getChildCount() - 1);
        Observable.from(memo.getTags())
                .forEach(tag -> {
                    View tagView = this.getActivity().getLayoutInflater()
                            .inflate(R.layout.tag_flow_item, this.tagsLayout, false);
                    TagViewHolder tagViewHolder = new TagViewHolder(tagView);
                    tagViewHolder.tag.setText(tag.getName());
                    // insert tag view before tag editor
                    this.tagsLayout.addView(tagView, this.tagsLayout.getChildCount() - 1);
                });
    }

    // implements MvpView

    @Override
    public void showMemo(@NonNull Memo memo) {
        this.memo = memo;
        editTitle.setText(memo.getTitle());
        editContent.setText(memo.getContent());
        showTagsView(memo);
    }
}
