
package com.r.library.common.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;

import com.r.library.common.util.LogUtils;
import com.r.library.common.util.MD5Utils;
import com.r.library.common.util.ThreadUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetHelper2 {
    private final String TAG = "NetHelper2";
    private OkHttpClient okHttpClient;
    private static NetHelper2 instance;
    private Builder builder;

    public static NetHelper2 getInstance() {
        if (null == instance) {
            synchronized (NetHelper2.class) {
                if (null == instance) {
                    instance = new NetHelper2.Builder().build();
                }
            }
        }
        return instance;
    }

    public Builder getBuilder() {
        return builder;
    }

    private NetHelper2() {
    }

    public HttpResponse request(String url) {
        return request(url, false, null);
    }

    public HttpResponse request(String url, boolean post, String postParams) {
        HttpResponse httpResponse = new HttpResponse();
        try {
            Response response;
            if (!post) {
                response = get(url);
            } else {
                response = post(url, postParams);
            }
            if (null == response) {
                return httpResponse;
            }
            httpResponse.responsCode = response.code();
            ResponseBody body = response.body();
            if (null != body) {
                httpResponse.response = response.body().string();
            }
            if (200 == httpResponse.responsCode) {
                httpResponse.success = true;
            } else {
                httpResponse.errMsg = response.message();
            }
        } catch (IOException e) {
            LogUtils.e(TAG, "request()", e);
        }
        return httpResponse;
    }

    public Response get(String url) throws IOException {
        return get(url, null);
    }

    public Response get(String url, Headers headers) throws IOException {
        Call call = getCall(url, headers);
        return call.execute();
    }

    public NetHelper2 getAsync(String url, Callback callback) {
        return getAsync(url, null, callback);
    }

    public NetHelper2 getAsync(String url, Headers headers, Callback callback) {
        Call call = getCall(url, headers);
        enqueueCall(call, callback);
        return this;
    }

    @NotNull
    private Call getCall(String url, Headers headers) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        return okHttpClient.newCall(request);
    }

    public Response post(String url, RequestBody body, Headers headers) throws IOException {
        Call call = postCall(url, body, headers);
        return call.execute();
    }

    public NetHelper2 postAsync(String url, RequestBody body, Headers headers,
            Callback callback) {
        Call call = postCall(url, body, headers);
        enqueueCall(call, callback);
        return this;
    }

    public Response post(String url, String body) throws IOException {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        return post(url, RequestBody.create(mediaType, body), null);
    }

    public Response post(String url, String mediaType, String body) throws IOException {
        return post(url, RequestBody.create(MediaType.parse(mediaType), body), null);
    }

    public Response post(String url, String mediaType, String body,
            Map<String, String> headerMap) throws IOException {
        Headers headers = Headers.of(headerMap);
        return post(url, RequestBody.create(MediaType.parse(mediaType), body), headers);
    }

    public NetHelper2 postAsync(String url, String body, Callback callback) {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        return postAsync(url, RequestBody.create(mediaType, body),
                null, callback);
    }

    public NetHelper2 postAsync(String url, String mediaType, String body,
            Callback callback) {
        return postAsync(url, RequestBody.create(MediaType.parse(mediaType), body),
                null, callback);
    }

    public NetHelper2 postAsync(String url, String mediaType, String body,
            Map<String, String> headerMap, Callback callback) {
        Headers headers = Headers.of(headerMap);
        return postAsync(url, RequestBody.create(MediaType.parse(mediaType), body),
                headers, callback);
    }

    public Response postFile(String url, File body) throws IOException {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", body.getName(), RequestBody.create(mediaType, body))
                .build();
        return post(url, requestBody, null);
    }

    public Response postFile(String url, File body, Map<String, String> headerMap)
            throws IOException {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", body.getName(), RequestBody.create(mediaType, body))
                .build();
        return post(url, requestBody, Headers.of(headerMap));
    }

    public NetHelper2 postFileAsync(String url, File body, Callback callback) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", body.getName(), RequestBody.create(mediaType, body))
                .build();
        return postAsync(url, requestBody, null, callback);
    }

    public NetHelper2 postFileAsync(String url, File body, Map<String, String> headerMap,
            Callback callback) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", body.getName(), RequestBody.create(mediaType, body))
                .build();
        return postAsync(url, requestBody, Headers.of(headerMap), callback);
    }

    public Response postForm(String url, FormBody body, Headers headers) throws IOException {
        Call call = postCall(url, body, headers);
        return call.execute();
    }

    public NetHelper2 postFormAsync(String url, FormBody body, Headers headers,
            Callback callback) {
        Call call = postCall(url, body, headers);
        enqueueCall(call, callback);
        return this;
    }

    public Response postMultipart(String url, MultipartBody body, Headers headers)
            throws IOException {
        Call call = postCall(url, body, headers);
        return call.execute();
    }

    public NetHelper2 postMultipartAsync(String url, MultipartBody body, Headers headers,
            Callback callback) {
        Call call = postCall(url, body, headers);
        enqueueCall(call, callback);
        return this;
    }

    private void enqueueCall(Call call, Callback callback) {
        call.enqueue(new OKHttpCallback(callback));
    }

    @NotNull
    private Call postCall(String url, RequestBody body, Headers headers) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        return okHttpClient.newCall(request);
    }

    public HttpDownload download(String url, String path, long downSpeed) throws IOException {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }

        return downloadImpl(url, path, downSpeed, null);
    }

    public void downloadAsync(String url, String path, long downSpeed,
            DownCallback downloadCallback) throws IllegalArgumentException {

        if (null == downloadCallback) {
            throw new IllegalArgumentException("Callback can not be null.");
        }

        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Url and path can not be null.");
        }

        Observable
                .create(new ObservableOnSubscribe<HttpDownload>() {
                    @Override
                    public void subscribe(ObservableEmitter<HttpDownload> emitter) {
                        try {
                            downloadImpl(url, path, downSpeed, downloadCallback);
                        } catch (Exception e) {
                            downloadCallback.onFailure(new Throwable(e));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private HttpDownload downloadImpl(String url, String path, long downSpeed,
            DownCallback downloadCallback) throws IOException {
        LogUtils.d(TAG, "downloadImpl() Thread: " + Thread.currentThread().getName());
        File file = initFilePath(path);
        if (file == null) {
            if (null != downloadCallback) {
                downloadCallback.onFailure(new Throwable("Init dir or file failed."));
            }
            return null;
        }

        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (response.code() != 200) {
            if (null != downloadCallback) {
                downloadCallback.onFailure(new Throwable("Request failed, code: " + response.code()));
            }
            return null;
        }

        if (null != downloadCallback && downloadCallback.isCancel()) {
            downloadCallback.onCancel();
            return null;
        }

        HttpDownload httpDownload = new HttpDownload();
        httpDownload.url = url;
        httpDownload.path = path;

        FileOutputStream fos = null;
        InputStream is = null;
        Exception ex = null;
        try {
            LogUtils.d(TAG, "Headers:\n" + response.request().headers());
            httpDownload.realUrl = response.request().url().toString();
            httpDownload.responsCode = response.code();
            is = response.body().byteStream();
            fos = new FileOutputStream(file);
            int num;
            long totalSize = 0L;
            long cursum = 0L;
            byte[] buf = new byte[1024 * 10];
            long speed = downSpeed == 0 ? 1024 * 1024 * 1000L : downSpeed;
            long startTime = SystemClock.elapsedRealtime();
            while ((num = is.read(buf)) != -1) {
                if (null != downloadCallback && downloadCallback.isCancel()) {
                    break;
                }
                fos.write(buf, 0, num);
                totalSize += num;
                cursum += num;
                if (cursum >= speed) {
                    long endTime = SystemClock.elapsedRealtime();
                    LogUtils.i(TAG, "use " + (endTime - startTime) + "ms, download size: "
                            + totalSize / 1024 + "KB");
                    ThreadUtils.sleep(1000L - (endTime - startTime));
                    cursum = 0L;
                }
                startTime = SystemClock.elapsedRealtime();
                if (null != downloadCallback && downloadCallback.isCancel()) {
                    break;
                }
            }
            if (fos.getFD().valid()) {
                fos.getFD().sync();
            }
            fos.flush();
            if (null != downloadCallback && downloadCallback.isCancel()) {
                LogUtils.d(TAG, "download() cancel.");
            } else {
                httpDownload.size = totalSize;
                httpDownload.localMd5 = MD5Utils.getFileMD5(path);
                httpDownload.success = true;
                LogUtils.d(TAG, "download() success.");
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "download() failed.", e);
            ex = e;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }

            if (null != downloadCallback) {
                if (downloadCallback.isCancel()) {
                    downloadCallback.onCancel();
                } else if (httpDownload.isSuccess()) {
                    downloadCallback.onSuccess(httpDownload);
                } else {
                    downloadCallback.onFailure(new Throwable(ex));
                }
            }

            return httpDownload;
        }
    }

    @Nullable
    private File initFilePath(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            if (!file.delete()) {
                LogUtils.e(TAG, "delete old file error: " + file.getPath());
                return null;
            }
        }
        String dir = file.getParent();
        LogUtils.i(TAG, "dir: " + dir);
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        if (file.createNewFile()) {
            LogUtils.i(TAG, "create new file: " + file.getPath());
        } else {
            LogUtils.e(TAG, "create new file error: " + file.getPath());
            return null;
        }
        return file;
    }

    private static class OKHttpCallback implements Callback {
        private Handler handler;
        private UIRunable uiRunable;

        public OKHttpCallback(Callback callback) {
            this.handler = new Handler(Looper.getMainLooper());
            this.uiRunable = new UIRunable(callback);
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            this.handler.post(uiRunable.onFailure(call, e));
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            this.handler.post(uiRunable.onResponse(call, response));
        }
    }

    private static class UIRunable implements Runnable {
        private Callback callback;
        private Call call;
        private Response response;
        private IOException ioException;

        private UIRunable(@NotNull Callback callback) {
            this.callback = callback;
        }

        public UIRunable onFailure(@NotNull Call call, @NotNull IOException e) {
            this.call = call;
            this.response = null;
            this.ioException = e;
            return this;
        }

        public UIRunable onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            this.call = call;
            this.response = response;
            this.ioException = null;
            return this;
        }

        @Override
        public void run() {
            try {
                if (response != null) {
                    callback.onResponse(call, response);
                } else {
                    callback.onFailure(call, ioException);
                }
            } catch (Exception e) {
            }
        }
    }

    public interface DownCallback {
        void cancel();

        boolean isCancel();

        void onCancel();

        void onSuccess(HttpDownload httpDownload);

        void onFailure(Throwable e);
    }

    public static class Builder {
        private OkHttpClient.Builder builder;
        private NetHelper2 okHttpHelper;
        private final int TIME_OUT = 10;

        public Builder() {
            this.okHttpHelper = new NetHelper2();
            this.builder = new OkHttpClient.Builder();
            connectTimeout(TIME_OUT, TimeUnit.SECONDS);
            readTimeout(TIME_OUT * 3, TimeUnit.SECONDS);
            writeTimeout(TIME_OUT * 6, TimeUnit.SECONDS);
        }

        public Builder readTimeout(long timeout, TimeUnit timeUnit) {
            builder.readTimeout(timeout, timeUnit);
            return this;
        }

        public Builder callTimeout(long timeout, TimeUnit timeUnit) {
            builder.callTimeout(timeout, timeUnit);
            return this;
        }

        public Builder connectTimeout(long timeout, TimeUnit timeUnit) {
            builder.connectTimeout(timeout, timeUnit);
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit timeUnit) {
            builder.writeTimeout(timeout, timeUnit);
            return this;
        }

        public Builder authenticator(Authenticator authenticator) {
            builder.authenticator(authenticator);
            return this;
        }

        public Builder certificatePinner(CertificatePinner certificatePinner) {
            builder.certificatePinner(certificatePinner);
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            builder.addInterceptor(interceptor);
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            builder.addNetworkInterceptor(interceptor);
            return this;
        }

        public NetHelper2 build() {
            okHttpHelper.builder = this;
            okHttpHelper.okHttpClient = builder.build();
            return okHttpHelper;
        }
    }

    public class HttpResponse {
        private boolean success;
        private int responsCode;
        private String response;
        private String errMsg;

        public HttpResponse() {
        }

        public boolean isSuccess() {
            return success;
        }

        public int getResponsCode() {
            return responsCode;
        }

        public String getResponse() {
            return response;
        }

        public String getErrMsg() {
            return errMsg;
        }

        @Override
        public String toString() {
            return "HttpResponse{" +
                    "success=" + success +
                    ", responsCode=" + responsCode +
                    ", response='" + response + '\'' +
                    ", errMsg='" + errMsg +
                    '}';
        }
    }

    public static class HttpDownload {
        private boolean success;
        private int responsCode;
        private long size;
        private String localMd5;
        private String path;
        private String url;
        private String realUrl;

        public HttpDownload() {
        }

        public boolean isSuccess() {
            return success;
        }

        public int getResponsCode() {
            return responsCode;
        }

        public long getSize() {
            return size;
        }

        public String getLocalMd5() {
            return localMd5;
        }

        public String getPath() {
            return path;
        }

        public String getUrl() {
            return url;
        }

        public String getRealUrl() {
            return realUrl;
        }

        @Override
        public String toString() {
            return "HttpDownload{" +
                    "success=" + success +
                    ", responsCode=" + responsCode +
                    ", size=" + size +
                    ", localMd5='" + localMd5 + '\'' +
                    ", path='" + path + '\'' +
                    ", url='" + url + '\'' +
                    ", realUrl='" + realUrl + '\'' +
                    '}';
        }
    }

}
