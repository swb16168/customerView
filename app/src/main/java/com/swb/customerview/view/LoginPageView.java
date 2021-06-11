package com.swb.customerview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swb.customerview.App;
import com.swb.customerview.R;

import java.lang.reflect.Field;

/**
 * @auther 72774
 * @date 2021/6/4  11:05
 * @description
 */
public class LoginPageView extends FrameLayout {
    private static final String TAG = "LoginPageView";
    public static final int SIZE_VERIFY_CODE = 4;
    private static final int PHONE_LENGTH = 11;
    private static final int DURATION_TIME_DEFAULT = 60; //默认倒计时60s
    private static final int INTERVAL_TIME_DEFAULT = 1; //倒计时间隔1S
    private int mMainColor;
    private int mVerifyCodeSize = SIZE_VERIFY_CODE;
    private CheckBox mIsCheckBox;
    private EditText mVerifytInput;
    private TextView mVerifytGet;
    private OnLoginPageActionListener mActionListener = null;
    private NumberKeyPad mNumberKeyPad;
    private EditText mPhoneInput;
    private TextView mLoginConfirm;

    private boolean isPhoneNumberOk = false;    //是否输入了11位电话号码
    private boolean isVerifytCodeOk = false;    //是否输入设定长度的验证码
    private boolean isAgreementOk = false;      //是否同意用户协议
    private CheckBox mReportCheckBox;
    public static final String PHONE_REGEXP = "^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\\d{8}$";
    private Handler mHandler;
    private boolean isCountDown = false;
    private boolean isSignin = false; //是否正在登录
    private TextView mUserReport;


    public LoginPageView(@NonNull Context context) {
        this(context, null);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        //初始化页面
        initView();

        initEvent();

    }

    private void initEvent() {

        mUserReport.setOnClickListener((v -> {
            if (mActionListener != null) {
                mActionListener.onOpenUserReport();
            }
        }));

        mVerifytGet.setOnClickListener((v -> {
            if (mActionListener != null) {
                mActionListener.onGetVerifytCodeClick(mPhoneInput.getText().toString());
                //开启倒计时
                //startCountDown();
                startCountdownTimer();
            }
        }));

        mLoginConfirm.setOnClickListener((v -> {
            if (mActionListener != null) {
                isSignin = true;
                handleUIState();
                mActionListener.onLoginClick(mPhoneInput.getText().toString(), mVerifytInput.getText().toString());
            }
        }));

        //mReportCheckBox.che

        //电话号码输入框文本改变事件
        mPhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phoneNumber = s.toString();
                isPhoneNumberOk = phoneNumber.length() == PHONE_LENGTH && phoneNumber.matches(PHONE_REGEXP);
                handleUIState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //验证码输入事件
        mVerifytInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isVerifytCodeOk = (s.length() == mVerifyCodeSize);
                handleUIState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //checkbox 选中事件
        mReportCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            isAgreementOk = isChecked;
            //根据是否选中

            handleUIState();
        }));

        //自定义键盘点击事件处理
        mNumberKeyPad.setOnKeyPadListener(new NumberKeyPad.OnKeyPadNumberListener() {
            @Override
            public void onNumberInput(int number) {
                //数字键点击
                EditText focusEdt = getFocusEdt();
                if (focusEdt != null) {
                    Editable editableText = focusEdt.getText();
                    int index = focusEdt.getSelectionEnd();
                    Log.d(TAG, "mNumberKeyPad onNumberInput: index----->" + index);
                    editableText.insert(index, String.valueOf(number));
                }
            }

            @Override
            public void ondDelete() {
                //退格键
                EditText focusEdt = getFocusEdt();
                if (focusEdt != null) {
                    Editable text = focusEdt.getText();
                    int index = focusEdt.getSelectionEnd();
                    Log.d(TAG, "mNumberKeyPad ondDelete: index----->" + index);
                    if (index > 0) {
                        text.delete(index - 1, index);
                    }
                }
            }
        });
    }

    private void initView() {
        //加载控件的布局
        LayoutInflater.from(getContext()).inflate(R.layout.view_login_page, this, true);

        //是否同意用户协议控件
        mIsCheckBox = findViewById(R.id.login_checkbox_report);

        mUserReport = findViewById(R.id.login_user_report);


        //验证码输入控件
        mVerifytInput = findViewById(R.id.login_verifyt_input);
        if (mMainColor != -1) {
            mIsCheckBox.setTextColor(mMainColor);
        }
        mVerifytInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mVerifyCodeSize)});

        //获取验证码控件
        mVerifytGet = findViewById(R.id.login_verifyt_get);

        //确定登录按钮
        mLoginConfirm = this.findViewById(R.id.login_confirm);

        //小键盘输入控件
        mNumberKeyPad = findViewById(R.id.login_number_pad);
        //电话号码输入框
        mPhoneInput = findViewById(R.id.login_phone_input);
        mPhoneInput.requestFocus();
        mReportCheckBox = findViewById(R.id.login_checkbox_report);

        //设置该控件获取焦点时不显示软键盘
        mPhoneInput.setShowSoftInputOnFocus(false);
        mVerifytInput.setShowSoftInputOnFocus(false);
        //禁用选中复制粘贴
        this.disableCopyAndPaste(mPhoneInput);
        this.disableCopyAndPaste(mVerifytInput);
    }

    private void initAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoginPageView);
        mMainColor = a.getColor(R.styleable.LoginPageView_mainColor, -1);
        mVerifyCodeSize = a.getInt(R.styleable.LoginPageView_verifyCodeSize, SIZE_VERIFY_CODE);
        durationTime = a.getInt(R.styleable.LoginPageView_countDownTime, DURATION_TIME_DEFAULT);
        intervalTime = a.getInt(R.styleable.LoginPageView_intervalTime, INTERVAL_TIME_DEFAULT);
        a.recycle();
    }

    /**
     * handler倒计时
     * 时长：durationTime
     * 间隔：step
     */
    private int durationTime = DURATION_TIME_DEFAULT;
    private int intervalTime = INTERVAL_TIME_DEFAULT;
    private int residueTime = durationTime;

    public void startCountDown() {
        isCountDown = true;
        mHandler = App.getHandler();
        /*mHandler.post(()->{
            residueTime = residueTime -intervalTime;
            if(residueTime > 0 ){
                mHandler.postDelayed(this::startCountDown,intervalTime * 1000);
            }
            Log.d(TAG, "startCountDown: residueTime--->" + residueTime);
        });*/
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                residueTime = residueTime - intervalTime;
                if (residueTime > 0) {
                    mHandler.postDelayed(this, intervalTime * 1000);
                    //控件显示倒计时并且不可点击
                    mVerifytGet.setText("(" + residueTime + ")S");
                    mVerifytGet.setEnabled(false);
                } else {
                    isCountDown = false;
                    mVerifytGet.setText(R.string.get_verifyt_text);
                    handleUIState();
                }
                Log.d(TAG, "startCountDown: residueTime--->" + residueTime);
            }
        });

    }

    private void startCountdownTimer() {
        isCountDown = false;
        new CountDownTimer(durationTime * 1000, intervalTime * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //控件显示倒计时并且不可点击
                mVerifytGet.setText("(" + millisUntilFinished / 1000 + ")S");
                mVerifytGet.setEnabled(false);
            }

            @Override
            public void onFinish() {
                isCountDown = false;
                mVerifytGet.setText(R.string.get_verifyt_text);
                handleUIState();
            }
        }.start();
    }


    public void onVerifytCodeError() {
        //提示用户 清空验证码
        isSignin = false;
        App.getHandler().post(() -> mVerifytInput.setText(""));
    }

    /**
     * 登录成功
     */
    public void onLoginSuccess() {
        isSignin = false;
        App.getHandler().post(() -> handleUIState());
    }


    /**
     * 根据状态条件 更新UI
     */
    private void handleUIState() {
        //验证码---》输入正确的手机号码
        mVerifytGet.setEnabled(isPhoneNumberOk && !isCountDown);
        //登录---》手机号码正确 && 验证码正确 && 同意用户协议 && 非登录状态
        mLoginConfirm.setEnabled(isPhoneNumberOk && isAgreementOk && isVerifytCodeOk && !isSignin);
        //XX协议的颜色改遍
        mUserReport.setTextColor(getResources().getColor(isAgreementOk ? R.color.mainColor : R.color.mainDeepColor));
    }

    /**
     * 设置登录页面监听器
     *
     * @param loginPageActionListener
     */
    public void setOnLoginPageActionListener(OnLoginPageActionListener loginPageActionListener) {
        this.mActionListener = loginPageActionListener;
    }


    /**
     * actionListener
     */
    public interface OnLoginPageActionListener {

        /**
         * 点击获取验证码
         *
         * @param phoneNum
         */
        void onGetVerifytCodeClick(String phoneNum);

        //todo: 用户协议点击


        /**
         * 登录
         *
         * @param phoneNum
         * @param verifytCode
         */
        void onLoginClick(String phoneNum, String verifytCode);

        /**
         * 打开用户协议
         */
        void onOpenUserReport();
    }


    public int getMainColor() {
        return mMainColor;
    }

    public void setMainColor(int mainColor) {
        mMainColor = mainColor;
    }

    public int getVerifyCodeSize() {
        return mVerifyCodeSize;
    }

    public void setVerifyCodeSize(int verifyCodeSize) {
        mVerifyCodeSize = verifyCodeSize;
    }


    /**
     * 设置用户协议
     *
     * @param reportText
     */
    public void setReportText(String reportText) {
        mUserReport.setText(reportText);
    }

    /**
     * 获取有焦点的输入框
     * 使用时判空
     *
     * @return null or EditText instance
     */
    private EditText getFocusEdt() {
        View view = this.findFocus();
        if (view instanceof EditText) {
            return (EditText) view;
        }
        return null;
    }


    //禁止EditText 选中复制粘贴
    @SuppressLint("ClickableViewAccessibility")
    public void disableCopyAndPaste(final EditText editText) {
        try {
            if (editText == null) {
                return;
            }

            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            editText.setLongClickable(false);
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // setInsertionDisabled when user touches the view
                        setInsertionDisabled(editText);
                    }
                    return false;
                }
            });
            editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
