package biz.info_cloud.simplememo.ui.fragment;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import biz.info_cloud.simplememo.R;
import biz.info_cloud.simplememo.domain.Memo;
import biz.info_cloud.simplememo.ui.presenter.EditPresenter;
import biz.info_cloud.simplememo.ui.presenter.Presenter;
import biz.info_cloud.simplememo.util.StringUtil;
import butterknife.Bind;
import butterknife.OnClick;

public class EditFragment extends BaseFragment implements EditPresenter.MvpView {
    public static final String BUNDLE_MEMO_ID = EditFragment.class.getCanonicalName() + ".BUNDLE_MEMO_ID";

    private Memo memo;

    @Inject
    EditPresenter editPresenter;

    @Bind(R.id.edit_title) EditText editTitle;
    @Bind(R.id.edit_content) EditText editContent;
    @Bind(R.id.save_button) Button btnSave;
    @Bind(R.id.cancel_button) Button btnCancel;

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
        String menuId = getArguments().getString(BUNDLE_MEMO_ID);
        if (!StringUtil.isNullOrEmpty(menuId)) {
            editPresenter.findMemo(menuId);
        }

        btnSave.setEnabled(editTitle.length() > 0);
    }

    @Override
    public Presenter getPresenter() {
        return editPresenter;
    }

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

    private void handleSaveButton(boolean enable) {
        btnSave.setEnabled(enable);
    }

    // implements MvpView

    @Override
    public void showMemo(@NonNull Memo memo) {
        this.memo = memo;
        editTitle.setText(memo.getTitle());
        editContent.setText(memo.getContent());
    }
}
