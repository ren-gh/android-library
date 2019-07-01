
package com.r.library.common.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.text.TextUtils;

import com.r.library.common.util.LogUtils;
import com.r.library.common.util.MD5Utils;
import com.r.library.common.util.ThreadUtils;

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

public class OKHttpHelper {
    private final String TAG = "OKHttpHelper";
    private OkHttpClient okHttpClient;
    private static OKHttpHelper instance;
    private Builder builder;

    public static OKHttpHelper getInstance() {
        if (null == instance) {
            synchronized (OKHttpHelper.class) {
                if (null == instance) {
                    instance = new OKHttpHelper.Builder().build();
                }
            }
        }
        return instance;
    }

    public Builder getBuilder() {
        return builder;
    }

    public static class Builder {
        private OkHttpClient.Builder builder;
        private OKHttpHelper okHttpHelper;
        private final int TIME_OUT = 10;

        public Builder() {
            this.okHttpHelper = new OKHttpHelper();
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

        public OKHttpHelper build() {
            okHttpHelper.builder = this;
            okHttpHelper.okHttpClient = builder.build();
            return okHttpHelper;
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

    private OKHttpHelper() {
    }

    public Response get(String url) throws IOException {
        return get(url, null);
    }

    public Response get(String url, Headers headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    public OKHttpHelper getAsync(String url, Callback callback) {
        return getAsync(url, null, callback);
    }

    public OKHttpHelper getAsync(String url, Headers headers, Callback callback) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return this;
    }

    public Response post(String url, RequestBody body, Headers headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    public OKHttpHelper postAsync(String url, RequestBody body, Headers headers, Callback callback) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
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

    public OKHttpHelper postAsync(String url, String body, Callback callback) {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        return postAsync(url, RequestBody.create(mediaType, body),
                null, callback);
    }

    public OKHttpHelper postAsync(String url, String mediaType, String body, Callback callback) {
        return postAsync(url, RequestBody.create(MediaType.parse(mediaType), body),
                null, callback);
    }

    public OKHttpHelper postAsync(String url, String mediaType, String body,
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

    public OKHttpHelper postFileAsync(String url, File body, Callback callback) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", body.getName(), RequestBody.create(mediaType, body))
                .build();
        return postAsync(url, requestBody, null, callback);
    }

    public OKHttpHelper postFileAsync(String url, File body, Map<String, String> headerMap,
            Callback callback) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", body.getName(), RequestBody.create(mediaType, body))
                .build();
        return postAsync(url, requestBody, Headers.of(headerMap), callback);
    }

    public Response postForm(String url, FormBody body, Headers headers) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    public OKHttpHelper postFormAsync(String url, FormBody body, Headers headers,
            Callback callback) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return this;
    }

    public Response postMultipart(String url, MultipartBody body, Headers headers)
            throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    public OKHttpHelper postMultipartAsync(String url, MultipartBody body, Headers headers,
            Callback callback) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        if (null != headers) {
            builder.headers(headers);
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return this;
    }

    public HttpDownload download(String url, String path, long downSpeed) throws IOException {
        HttpDownload httpDownload = new HttpDownload();
        httpDownload.url = url;
        httpDownload.path = path;
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return httpDownload;
        }

        File file = new File(path);
        if (file.exists()) {
            if (!file.delete()) {
                LogUtils.e(TAG, "delete old file error: " + file.getPath());
                return httpDownload;
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
            return httpDownload;
        }

        Request.Builder builder = new Request.Builder()
                .url(url);
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        downloadImpl(path, downSpeed, httpDownload, file, response);

        return httpDownload;
    }

    private void downloadImpl(String path, long downSpeed, HttpDownload httpDownload, File file, Response response) {
        RandomAccessFile fos = null;
        InputStream is = null;
        try {
            LogUtils.d(TAG, "Headers:\n" + response.request().headers());
            httpDownload.realUrl = response.request().url().toString();
            httpDownload.responsCode = response.code();
            is = response.body().byteStream();
            httpDownload.size = response.body().contentLength();
            fos = new RandomAccessFile(file, "rw");
            int num = 0;
            long totalSize = 0L;
            long cursum = 0L;
            byte[] buf = new byte[1024 * 10];
            long speed = downSpeed == 0 ? 1024 * 1024 * 1000L : downSpeed;
            while ((num = is.read(buf)) != -1) {
                fos.write(buf, 0, num);
                totalSize += num;
                cursum += num;
                if (cursum >= speed) {
                    LogUtils.i(TAG, "download size: " + totalSize / 1024 + "KB");
                    ThreadUtils.sleep(1000);
                    cursum = 0L;
                }
            }
            if (fos.getFD().valid()) {
                fos.getFD().sync();
            }
            httpDownload.localMd5 = MD5Utils.getFileMD5(path);
            httpDownload.success = true;
            LogUtils.d(TAG, "download() success.");
        } catch (Exception e) {
            LogUtils.e(TAG, "download() failed.", e);
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
        }
    }
}
