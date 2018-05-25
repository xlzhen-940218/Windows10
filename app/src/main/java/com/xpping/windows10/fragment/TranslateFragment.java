package com.xpping.windows10.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;

import android.widget.TextView;


import com.baidu.translate.entity.TransEntity;
import com.baidu.translate.utils.PlayMp3Utils;
import com.baidu.translate.web.NetWorkResponseListener;
import com.baidu.translate.web.RequestHttpUtils;
import com.xpping.windows10.R;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.widget.MenuSelectDialog;
import com.xpping.windows10.widget.ToastView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
*xlzhen 2018/4/27
* 翻译
*/
public class TranslateFragment extends BaseFragment {
    private EditText editText;
    private TextView spinnerFrom, spinnerTo;
    private String[] la_simple, languages;
    private String from, to;

    private MenuSelectDialog spFrom, spTo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        //setActionBarTitle("翻译");
        editText = findViewById(R.id.edit_query);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        findViewById(R.id.mp3_result).setOnClickListener(this);
        findViewById(R.id.translate_button).setOnClickListener(this);

        la_simple = getResources().getStringArray(R.array.languages_simple);
        languages = getResources().getStringArray(R.array.languages);

        spinnerFrom.setText(languages[22] + "      ▾");
        from = la_simple[22];

        spinnerTo.setText(languages[23] + "      ▾");
        to = la_simple[23];
    }

    @Override
    protected void initWidget() {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && v.getText().length() > 0) {
                    getTransData(editText.getText().toString());
                }
                return false;
            }
        });
        spinnerFrom.setOnClickListener(this);
        spinnerTo.setOnClickListener(this);

    }

    public void getTransData(String text) {
        String url = "http://fanyi.baidu.com/basetrans";
        Map<String, String> map = new HashMap<>();
        map.put("query", text);
        map.put("from", from);
        map.put("to", to);
        RequestHttpUtils.postData(getContext(), url, map, TransEntity.class, new NetWorkResponseListener<TransEntity>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TransEntity bean) {
                //将翻译结果呈现给大家
                if(bean==null||bean.getTrans()==null) {
                    ToastView.getInstaller(getContext()).setText("翻译出错").show();
                    return;
                }
                ((TextView) findViewById(R.id.trans_result)).setText("翻译结果：" + bean.getTrans().get(0).getDst());
                String pinyin = bean.getDict().getSymbols().get(0).getWord_symbol();
                if (pinyin == null && bean.getPhonetic() != null && bean.getPhonetic().size() > 0) {
                    pinyin = "";
                    for (int i = 0; i < bean.getPhonetic().size(); i++)
                        pinyin += bean.getPhonetic().get(i).getTrg_str();
                }

                if (pinyin == null)
                    pinyin = "无结果";

                ((TextView) findViewById(R.id.pinyin_result)).setText("拼音：" + pinyin);

                findViewById(R.id.mp3_result).setTag(bean.getDict().getSymbols().get(0).getSymbol_mp3());
                ((TextView) findViewById(R.id.mp3_result)).setText("发音："
                        + (findViewById(R.id.mp3_result).getTag() !=null
                        &&((String)findViewById(R.id.mp3_result).getTag()).contains("http") ? "没有音源" : "点击播放"));

                ((TextView) findViewById(R.id.kingsoft_result)).setText("");
                if (bean.getDict().getWord_means() != null&&bean.getDict().getWord_means().length>0) {
                    ((TextView) findViewById(R.id.kingsoft_result)).setText("金山词霸结果：\n");
                    for (String mean : bean.getDict().getWord_means())
                        ((TextView) findViewById(R.id.kingsoft_result))
                                .setText(((TextView) findViewById(R.id.kingsoft_result)).getText().toString() + "\n" + mean);
                }

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return setFragmentView(inflater.inflate(R.layout.fragment_translate, container, false));
    }

    @Override
    protected void onClick(View view, int viewId) {
        switch (viewId) {
            case R.id.mp3_result:
                if (view.getTag() !=null &&((String)view.getTag()).contains("http")) {
                    PlayMp3Utils.installMp3(getContext(), (String) view.getTag());

                }
                break;
            case R.id.translate_button:
                if (editText.getText().length() > 0)
                    getTransData(editText.getText().toString());
                break;
            case R.id.spinnerFrom:
                if (spFrom == null) {
                    spFrom = new MenuSelectDialog(getActivity(), (int) view.getX(), DensityUtils.dp2px(90));
                    spFrom.setMenuData(Arrays.asList(languages), new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            from = la_simple[position];
                            spinnerFrom.setText(languages[position] + "      ▾");
                            spFrom.dismiss();
                        }
                    });
                }
                spFrom.show();
                break;
            case R.id.spinnerTo:
                if (spTo == null) {
                    spTo = new MenuSelectDialog(getActivity(), (int) view.getX(), DensityUtils.dp2px(90));
                    spTo.setMenuData(Arrays.asList(languages), new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            to = la_simple[position];
                            spinnerTo.setText(languages[position] + "      ▾");
                            spTo.dismiss();
                        }
                    });
                }
                spTo.show();
                break;
        }
    }

    @Override
    public boolean onBackKey() {
        return true;
    }
}
