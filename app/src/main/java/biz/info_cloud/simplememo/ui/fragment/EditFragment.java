package biz.info_cloud.simplememo.ui.fragment;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
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
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import rx.Observable;

public class EditFragment extends BaseFragment implements EditPresenter.MvpView {
    public static final String BUNDLE_MEMO_ID = EditFragment.class.getCanonicalName() + ".BUNDLE_MEMO_ID";

    private Memo memo;
    private boolean dirty = false;

    @Inject
    EditPresenter editPresenter;

    @Bind(R.id.edit_title) EditText editTitle;
    @Bind(R.id.edit_content) EditText editContent;
    @Bind(R.id.save_button) Button btnSave;
    @Bind(R.id.cancel_button) Button btnCancel;
    @Bind(R.id.tags_layout) FlowLayout tagsLayout;
    @Bind(R.id.edit_tag) EditText editTag;

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

        btnSave.setEnabled(editTitle.length() > 0);
        this.memo = createNewMemo();
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
        if (!editTitle.getText().toString().equals(memo.getTitle())) {
            this.dirty = true;
            this.memo.setTitle(editTitle.getText().toString());
        }
        if (!editContent.getText().toString().equals(memo.getContent())) {
            this.dirty = true;
            this.memo.setContent(editContent.getText().toString());
        }
        if (this.dirty == true) {
            editPresenter.updateMemo(memo);
        }
    }

    @OnFocusChange(R.id.edit_title)
    void onTitleFocusChanged(boolean hasFocus) {
        if (!hasFocus && editTitle != null) {
            editPresenter.setTitle(editTitle.getText().toString(), memo);
        }
    }

    @OnTextChanged(R.id.edit_title)
    void OnTitleTextChanged(CharSequence text) {
        handleSaveButton(text.length() > 0);
    }

    @OnFocusChange(R.id.edit_content)
    void onContentFocusChanged(boolean hasFocus) {
        if (!hasFocus && editContent != null) {
            editPresenter.setContent(editContent.getText().toString(), memo);
        }
    }

    @OnEditorAction(R.id.edit_tag)
    boolean onEditTagAction(KeyEvent key) {
        if (editTag.length() < 1) {
            return true;
        }

        this.dirty = true;
        editPresenter.addTag(editTag.getText().toString(), this.memo);
        return true;

    }


    private Memo createNewMemo() {
        return new Memo(editTitle.getText().toString(), editContent.getText().toString());
    }

    // fragment intrinsic functions

    private void handleSaveButton(boolean enable) {
        btnSave.setEnabled(enable);
    }

    private void showTagsView(@NonNull Memo memo) {
        // remove tags view except tag editor
        this.tagsLayout.removeAllViews();
        Observable.from(memo.getTags())
                .forEach(tag -> {
                    View tagView = this.getActivity().getLayoutInflater()
                            .inflate(R.layout.tag_flow_item, this.tagsLayout, false);
                    TagViewHolder tagViewHolder = new TagViewHolder(tagView, true);
                    tagViewHolder.tag.setText(tag.getName());

                    tagViewHolder.delte.setTag(tag.getName());
                    tagViewHolder.delte.setOnClickListener(view -> {
                        String deleteTag = (String) view.getTag();
                        this.dirty = true;
                        editPresenter.deleteTag(deleteTag, memo);
                    });

                    this.tagsLayout.addView(tagView);
                });
    }

    // implements MvpView

    @Override
    public void showMemo(@NonNull Memo memo) {
        this.memo = memo;
        editTitle.setText(memo.getTitle());
        editContent.setText(memo.getContent());
        editTag.setText("");
        showTagsView(memo);
    }
}
