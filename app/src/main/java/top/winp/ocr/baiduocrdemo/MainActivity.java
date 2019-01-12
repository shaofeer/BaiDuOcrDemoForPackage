package top.winp.ocr.baiduocrdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.baidu.ocr.ui.camera.CameraActivity;

import java.io.File;


/**
 * @description: description:  https://ai.baidu.com/docs#/OCR-Android-SDK/top
 * <p>
 * @author: pyfysf
 * <p>
 * @qq: 337081267
 * <p>
 * @CSDN: http://blog.csdn.net/pyfysf
 * <p>
 * @blog: http://wintp.top
 * <p>
 * @email: pyfysf@163.com
 * <p>
 * @time: 2019/1/12
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_GENERAL = 100;
    private Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
    }

    private void initView() {
        Button btn_init = findViewById(R.id.btn_init);
        Button btn_select = findViewById(R.id.btn_select);


        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageUI();
            }
        });


        btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OCR.getInstance(mContext).initAccessToken(new OnResultListener<AccessToken>() {
                    @Override
                    public void onResult(AccessToken result) {
                        // 调用成功，返回AccessToken对象
                        final String token = result.getAccessToken();

                        Log.e("MainActivity", "MainActivity onResult()" + token);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onError(OCRError error) {
                        // 调用失败，返回OCRError子类SDKError对象
                        error.printStackTrace();
                    }
                }, getApplicationContext());
            }
        });
    }

    /**
     * 打开百度提供的UI选择图片
     */
    private void openImageUI() {
        // 生成intent对象
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);

        // 设置临时存储
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());


        startActivityForResult(intent, REQUEST_CODE_GENERAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取调用参数
        String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
        // 通过临时文件获取拍摄的图片
        String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
        // 判断拍摄类型（通用，身份证，银行卡等）
        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
            // 判断是否是身份证正面
            if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                // 获取图片文件调用sdk数据接口，见数据接口说明
            }
        }
    }


    public void getData(String filePath) {

        // 通用文字识别参数设置
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));

        final StringBuffer sb = new StringBuffer();

// 调用通用文字识别服务
        OCR.getInstance(this).recognizeGeneralBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                // 调用成功，返回GeneralResult对象
                for (WordSimple wordSimple : result.getWordList()) {
                    // wordSimple不包含位置信息
                    WordSimple word = wordSimple;
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                // json格式返回字符串
                //listener.onResult(result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
            }
        });


        // 通用文字识别参数设置
        //GeneralBasicParams param = new GeneralBasicParams();
        //param.setDetectDirection(true);
        //param.setImageFile(new File(filePath));
        //final StringBuffer sb = new StringBuffer();
        //
        //// 调用通用文字识别服务
        //OCR.getInstance(this).recognizeAccurateBasic(param, new OnResultListener<GeneralResult>() {
        //    @Override
        //    public void onResult(GeneralResult result) {
        //        // 调用成功，返回GeneralResult对象
        //        for (WordSimple wordSimple : result.getWordList()) {
        //            // wordSimple不包含位置信息
        //            WordSimple word = wordSimple;
        //            sb.append(word.getWords());
        //            sb.append("\n");
        //        }
        //        // json格式返回字符串
        //        //listener.onResult(result.getJsonRes());
        //    }
        //
        //    @Override
        //    public void onError(OCRError error) {
        //        // 调用失败，返回OCRError对象
        //    }
        //});

    }

}
