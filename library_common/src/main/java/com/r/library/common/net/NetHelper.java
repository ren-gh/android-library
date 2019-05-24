
package com.r.library.common.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.r.library.common.util.BitmapUtils;
import com.r.library.common.util.FileUtils;
import com.r.library.common.util.LogUtils;
import com.r.library.common.util.MD5Utils;
import com.r.library.common.util.ThreadUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

/**
 * Created by rengh on 17-5-29.
 */
public class NetHelper {
    private final String TAG = "NetHelper";
    private int mTimeOut = 1000 * 5;

    public NetHelper() {
    }

    public NetHelper setTimeOut(int mills) {
        mTimeOut = mills;
        return this;
    }

    public HttpResponse request(String url) {
        return request(url, false, null);
    }

    public HttpResponse request(String url, boolean post, String postParams) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = requestImpl(url, post, postParams);
        } catch (CertPathValidatorException e) {
            LogUtils.e(TAG, "CertPathValidatorException: ", e);
            if (url.startsWith("https://")) {
                url = url.replace("https://", "http://");
                httpResponse = request(url, post, postParams);
            }
        }
        return httpResponse;
    }

    public HttpResponse getBitmap(Context context, String url) {
        try {
            return getImg(context, url);
        } catch (CertPathValidatorException e) {
            LogUtils.e(TAG, "CertPathValidatorException: ", e);
            if (url.startsWith("https://")) {
                url = url.replace("https://", "http://");
                return getBitmap(context, url);
            }
        }
        return null;
    }

    public HttpUpload upload(String url, String filePath) {
        HttpUpload httpUpload = new HttpUpload();
        httpUpload.path = filePath;
        httpUpload.md5 = MD5Utils.getFileMD5(filePath);
        httpUpload.size = FileUtils.getFileSize(filePath);
        final String BOUNDARYSTR = createBoundary();
        final String END = "\r\n";
        final String LAST = "--";
        FileInputStream fis = null;// 文件输入流
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setChunkedStreamingMode(51200); // 128K
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Expect", "100-continue");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARYSTR);
            connection.setRequestProperty("User-Agent", "Android Client");

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append(LAST + BOUNDARYSTR + END);
            sb.append("Content-Disposition:file;name=\"").append(filePath).append("\";").append(END);
            sb.append("Content-Disposition:file;filename=\"").append(filePath).append("\";").append(END);
            sb.append("Content-Type:application/octet-stream").append(END);
            sb.append("Content-Transfer-Encoding: binary").append(END);
            sb.append(END);

            dos.write(sb.toString().getBytes("utf-8"));

            if (FileUtils.isExists(filePath)) {
                fis = new FileInputStream(new File(filePath));
            }
            if (fis != null) {
                byte[] b = new byte[1024];
                int len;
                while ((len = fis.read(b)) != -1) {
                    dos.write(b, 0, len);
                }
                dos.write(END.getBytes());
                httpUpload.success = true;
            } else {
                httpUpload.success = false;
            }
            dos.write((LAST + BOUNDARYSTR + LAST + END).getBytes());
            dos.flush();

            httpUpload.responsCode = connection.getResponseCode();

            InputStream in = connection.getInputStream();
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader read = new BufferedReader(inReader);
            StringBuilder builder = new StringBuilder();
            String tmp;
            while (null != (tmp = read.readLine())) {
                builder.append(tmp);
            }
            httpUpload.response = builder.toString();
        } catch (MalformedURLException e) {
            httpUpload.success = false;
            LogUtils.i(TAG, "===== MalformedURLException:", e);
        } catch (IOException e) {
            httpUpload.success = false;
            LogUtils.i(TAG, "===== IOException:", e);
        } catch (Exception e) {
            httpUpload.success = false;
            LogUtils.i(TAG, "===== Exception:", e);
        }
        return httpUpload;
    }

    public HttpDownload download(String downloadUrl, String path, int downloadSpeed) {
        return download(downloadUrl, path, downloadSpeed, false);
    }

    public HttpDownload download(String downloadUrl, String path, int downloadSpeed, boolean usePost) {
        HttpDownload httpDownload = new HttpDownload();
        httpDownload.url = downloadUrl;
        httpDownload.path = path;
        if (TextUtils.isEmpty(downloadUrl) || TextUtils.isEmpty(path)) {
            return httpDownload;
        }

        RandomAccessFile fos = null;
        InputStream is = null;
        HttpURLConnection conn;

        try {
            URL url = new URL(downloadUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (usePost) {
                conn.setRequestMethod("POST");
                LogUtils.i(TAG, "===== method: POST");
            } else {
                conn.setRequestMethod("GET");
                LogUtils.i(TAG, "===== method: GET");
            }
            conn.setConnectTimeout(mTimeOut);
            conn.setReadTimeout(mTimeOut);

            String contentType = conn.getHeaderField("Content-Type");
            LogUtils.i(TAG, "===== contentType:" + contentType);

            long fileSize = conn.getContentLength();
            LogUtils.i(TAG, "===== download file's size:" + fileSize / 1024 + " KB");
            httpDownload.size = fileSize;
            if (httpDownload.size == 0) {
                return httpDownload;
            }

            httpDownload.responsCode = conn.getResponseCode();

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

            is = conn.getInputStream();
            fos = new RandomAccessFile(file, "rw");
            byte[] buf = new byte[1024 * 10];
            long totalSize = 0;
            int num = 0;
            long cursum = 0;
            int speed = downloadSpeed;
            while ((num = is.read(buf)) != -1) {
                fos.write(buf, 0, num);
                totalSize += num;
                cursum += num;
                // long bootLength = SystemClock.elapsedRealtime();
                // boolean isBootedFiveMinutes = bootLength > 1000 * 60 * 5;
                // if (speed < downloadSpeed && isBootedFiveMinutes) {
                // speed = downloadSpeed;
                // LogUtils.i(TAG, "===== Change speed: " + speed / 1024 + "KB");
                // }
                if (cursum >= speed) {
                    LogUtils.i(TAG, "download size: " + totalSize / 1024 + "KB");
                    ThreadUtils.sleep(1000);
                    cursum = 0;
                }
            }
            if (fos.getFD().valid()) {
                fos.getFD().sync();
            }
            httpDownload.localMd5 = MD5Utils.getFileMD5(path);
            httpDownload.success = true;
        } catch (IllegalArgumentException e) {
            LogUtils.i(TAG, "===== IllegalArgumentException:", e);
        } catch (MalformedURLException e) {
            LogUtils.i(TAG, "===== MalformedURLException:", e);
        } catch (IOException e) {
            LogUtils.i(TAG, "===== IOException:", e);
        } catch (ClassCastException e) {
            LogUtils.i(TAG, "===== ClassCastException:", e);
        } catch (Exception e) {
            LogUtils.i(TAG, "===== Exception:", e);
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }

        return httpDownload;
    }

    private HttpResponse requestImpl(String mUrl, boolean post, String postParam) throws CertPathValidatorException {
        HttpResponse httpResponse = new HttpResponse();
        HttpURLConnection urlConnection = null;
        DataOutputStream dos = null;
        InputStream in = null;
        InputStreamReader inReader = null;
        BufferedReader read = null;
        try {
            // 不使用证书
            URL url = new URL(mUrl);
            trustAllHosts();
            if ("https".equals(url.getProtocol().toLowerCase(Locale.getDefault()))) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(DO_NOT_VERIFY);
                urlConnection = httpsURLConnection;
            } else {
                urlConnection = (HttpURLConnection) url.openConnection();
            }
            urlConnection.setDoInput(true); // 需要输入
            urlConnection.setUseCaches(false); // 不允许缓存
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setConnectTimeout(mTimeOut);
            urlConnection.setReadTimeout(mTimeOut);
            // urlConnection.connect();
            if (post) {
                urlConnection.setRequestMethod("POST"); // 设置POST方式连接
                if (null != postParam) {
                    urlConnection.setDoOutput(true); // 需要输出
                    dos = new DataOutputStream(urlConnection.getOutputStream());
                    dos.write(postParam.getBytes());
                    dos.flush();
                    dos.close();
                }
            } else {
                urlConnection.setRequestMethod("GET");
            }
            httpResponse.responsCode = urlConnection.getResponseCode();
            in = urlConnection.getInputStream();
            inReader = new InputStreamReader(in);
            read = new BufferedReader(inReader);
            StringBuilder builder = new StringBuilder();
            String tmp = null;
            while (null != (tmp = read.readLine())) {
                builder.append(tmp);
            }
            httpResponse.response = builder.toString();
            httpResponse.success = true;
            builder = null;
        } catch (IllegalMonitorStateException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "IllegalMonitorStateException: " + httpResponse.errMsg);
        } catch (IllegalArgumentException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "IllegalArgumentException: " + httpResponse.errMsg);
        } catch (RuntimeException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "RuntimeException: " + httpResponse.errMsg);
        } catch (UnknownHostException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "UnknownHostException: " + httpResponse.errMsg);
        } catch (ConnectException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "ConnectException: " + httpResponse.errMsg);
        } catch (SocketTimeoutException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "SocketTimeoutException: " + httpResponse.errMsg);
        } catch (MalformedURLException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "MalformedURLException: " + httpResponse.errMsg);
        } catch (IOException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "IOException.", e);
        } catch (Exception e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "Exception.", e);
        } finally {
            if (null != read) {
                try {
                    read.close();
                } catch (IOException e) {
                }
                read = null;
            }
            if (null != inReader) {
                try {
                    inReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inReader = null;
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (null != urlConnection) {
                urlConnection.disconnect();
                urlConnection = null;
            }
        }

        if (null != httpResponse.response && httpResponse.response.contains("&")) {
            httpResponse.response = httpResponse.response.replace("&#039;", "'");
            httpResponse.response = httpResponse.response.replace("&quot;", "\"");
            httpResponse.response = httpResponse.response.replace("&lt;", "<");
            httpResponse.response = httpResponse.response.replace("&gt;", ">");
            httpResponse.response = httpResponse.response.replace("&amp;", "&");
        }

        return httpResponse;
    }

    private HttpResponse getImg(Context context, String mUrl) throws CertPathValidatorException {
        HttpResponse httpResponse = new HttpResponse();
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        InputStreamReader inReader = null;
        httpResponse.bitmap = null;
        try {
            // 不使用证书
            URL url = new URL(mUrl);
            trustAllHosts();
            if ("https".equals(url.getProtocol().toLowerCase(Locale.getDefault()))) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(DO_NOT_VERIFY);
                urlConnection = httpsURLConnection;
            } else {
                urlConnection = (HttpURLConnection) url.openConnection();
            }
            urlConnection.setConnectTimeout(mTimeOut);
            urlConnection.setReadTimeout(mTimeOut);
            urlConnection.connect();
            httpResponse.responsCode = urlConnection.getResponseCode();
            in = urlConnection.getInputStream();
            httpResponse.bitmap = BitmapUtils.readBitMap(context, in);
            httpResponse.success = true;
        } catch (UnknownHostException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "UnknownHostException: " + httpResponse.errMsg);
        } catch (ConnectException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "ConnectException: " + httpResponse.errMsg);
        } catch (SocketTimeoutException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "SocketTimeoutException: " + httpResponse.errMsg);
        } catch (MalformedURLException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "MalformedURLException: " + httpResponse.errMsg);
        } catch (IOException e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "IOException.", e);
        } catch (Exception e) {
            httpResponse.errMsg = e.getMessage();
            LogUtils.e(TAG, "Exception.", e);
        } finally {
            if (null != inReader) {
                try {
                    inReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }

        return httpResponse;
    }

    private String createBoundary() {
        Random r = new Random();
        String allowed = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < 32; ++i) {
            b.append(allowed.charAt(r.nextInt(allowed.length())));
        }

        return b.toString();
    }

    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    @SuppressLint("TrulyRandom")
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[] {};
                    }

                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取需要计算签名的参数(去空)
     *
     * @return 去空后的参数拼接结果
     */
    private static String getRealParams(String params) {
        if (null == params) {
            return null;
        }
        String[] items = params.split("&");
        String newParams = "";
        if (null != items && items.length > 0) {
            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                if (null == item || !item.endsWith("=")) {
                    if (i != 0) {
                        item = "&" + item;
                    }
                    newParams += item;
                }
            }
        }
        return newParams;
    }

    public class HttpUpload {
        private boolean success;
        private int responsCode;
        private long size;
        private String md5;
        private String path;
        private String response;

        public boolean isSuccess() {
            return success;
        }

        public int getResponsCode() {
            return responsCode;
        }

        public long getSize() {
            return size;
        }

        public String getMd5() {
            return md5;
        }

        public String getPath() {
            return path;
        }

        public String getResponse() {
            return response;
        }

        @Override
        public String toString() {
            return "HttpUpload{" +
                    "success=" + success +
                    ", responsCode=" + responsCode +
                    ", size=" + size +
                    ", md5='" + md5 + '\'' +
                    ", path='" + path + '\'' +
                    ", response='" + response + '\'' +
                    '}';
        }
    }

    public class HttpDownload {
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

    public class HttpResponse {
        private boolean success;
        private int responsCode;
        private String response;
        private String errMsg;
        private Bitmap bitmap;

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

        public Bitmap getBitmap() {
            return bitmap;
        }

        @Override
        public String toString() {
            return "HttpResponse{" +
                    "success=" + success +
                    ", responsCode=" + responsCode +
                    ", response='" + response + '\'' +
                    ", errMsg='" + errMsg + '\'' +
                    ", bitmap=" + bitmap +
                    '}';
        }
    }
}
