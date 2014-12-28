package edu.ubbcluj.canvasAndroid.backend.util.network;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

//Custom HttpClient to solve ssl problems
public class RestHttpClient {

	public static HttpClient getNewHttpClient(Certificate ca) {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);

			Log.d("Rest", "ok1");

			SSLSocketFactory sf = new RestSSLSocketFactory(keyStore);
			// sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			Log.d("Rest", "ok2");

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			Log.d("Rest", "ok3");

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			Log.d("Rest", "ok4");

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			Log.d("Rest", "ok5");

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			Log.d("Rest", "DefaultHttpClient");
			return new DefaultHttpClient();
		}
	}

}
