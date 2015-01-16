package edu.ubbcluj.canvasAndroid.util.network;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

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

public class RestHttpClient {

	public static HttpClient getNewHttpClient() {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, null);
			keyStore.setCertificateEntry("addtrust", readCertificate("addtrust.cer"));
			keyStore.setCertificateEntry("usertrustrsa", readCertificate("usertrustrsa.cer"));
			keyStore.setCertificateEntry("globessl", readCertificate("globessl.cer"));
			keyStore.setCertificateEntry("canvascert", readCertificate("canvascert.cer"));
			keyStore.setCertificateEntry("canvascertselfsigned", readCertificate("canvascertss.cer"));

			SSLSocketFactory sf = new RestSSLSocketFactory(keyStore);
			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			Log.d("Rest", "DefaultHttpClient");
			return new DefaultHttpClient();
		}
	}
	
	private static Certificate readCertificate(String name) {
		String file = "res/raw/" + name;
		InputStream caInput = RestHttpClient.class.getClassLoader().getResourceAsStream(file);
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return cf.generateCertificate(caInput);
		} catch (CertificateException e) {
			Log.e("SSL", "Faild to generate certificate: " + name + "\n" + e.toString());
			
		} finally {
			try {
				caInput.close();
			} catch (IOException e) {
				Log.e("SSL", "Can't close certificate file");
			}
		}
		
		return null;
	}

}
