package fr.viewpoint.utils.rest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Rest JSON Client
 * 
 * @author jpoirier
 * 
 */
public class Client {

	private static final Logger log = LoggerFactory.getLogger(Client.class);

	/**
	 * The call options
	 */
	private Config config;

	/**
	 * Construct a new Rest client
	 * 
	 */
	public Client() {
		super();
		this.config = new Config();
	}

	/**
	 * Construct a new Rest client
	 * 
	 * @param config
	 *            The rest configuration
	 */
	public Client(Config config) {
		super();
		this.config = config;
	}

	/**
	 * An alternative to the URL
	 */
	private static InputStream inputStream = null;

	/**
	 * The JSON Object mapper used to extract json
	 */
	private ObjectMapper mapper = new ObjectMapper();

	private InputStream getStream(URL url, String method, String params,
			HttpURLConnection conn) {
		if (log.isDebugEnabled()) {
			log.debug("Calling " + url);
		}

		if (null == method) {
			throw new IllegalArgumentException("Bad method");
		}
		if (null == url) {
			throw new IllegalArgumentException("Bad url");
		}

		try {
			InputStream stream = null;

			// Do the query
			if (null == inputStream) {
				URLConnection urlConn = null;
				// Push proxy configuration
				if (!config.hasProxy()) {
					urlConn = url.openConnection();
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Add proxy : " + config.getProxyHost() + ":"
								+ config.getProxyPort());
					}
					Proxy proxy = new Proxy(Proxy.Type.HTTP,
							new InetSocketAddress(config.getProxyHost(),
									config.getProxyPort()));
					urlConn = url.openConnection(proxy);
				}

				// Let us manage local file
				if (urlConn instanceof HttpURLConnection) {

					conn = (HttpURLConnection) urlConn;
					conn.setRequestMethod(method);
					conn.setDoOutput(true);
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestProperty("Accept", "application/json");

					if (null != config.getTimeout()) {
						conn.setConnectTimeout(config.getTimeout());
					}

					if (log.isDebugEnabled()) {
						log.debug("Timeout set to [" + conn.getConnectTimeout()
								+ "]");
					}

					if (null != params) {
						conn.setRequestProperty("Content-Length",
								Integer.toString(params.length()));
					}

					// Manage basic authentication
					if (config.hasBasicAuth()) {

						if (log.isDebugEnabled()) {
							log.debug("Add basic authentication to username : "
									+ config.getBasicAuthLogin());
						}

						String authString = config.getBasicAuthLogin() + ":"
								+ config.getBasicAuthPassword();
						byte[] authEncBytes = Base64.encodeBase64(authString
								.getBytes());
						String authStringEnc = new String(authEncBytes);
						conn.setRequestProperty("Authorization", "Basic "
								+ authStringEnc);
					}

					// Add params
					if (null != params && !"".equals(params)) {
						OutputStream os = conn.getOutputStream();
						BufferedWriter writer = new BufferedWriter(
								new OutputStreamWriter(os, "UTF-8"));
						writer.write(params);
						writer.close();
						os.close();
					}

					if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
						throw new RuntimeException(
								"Failed : HTTP error code : "
										+ conn.getResponseCode());
					}

					urlConn = conn;
				}

				stream = (InputStream) urlConn.getContent();
			} else {

				// Load the given input stream for test
				stream = inputStream;

				// Clean reference
				inputStream = null;
			}

			return stream;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public <T> T doCall(URL url,
			@SuppressWarnings("rawtypes") TypeReference valueTypeRef,
			String method, String params) {

		// Read content
		if (null == mapper) {
			mapper = new ObjectMapper();
		}

		// Allow to not check if a field is present in stream but not in
		// class
		if (config.isDisableMapperCheck()) {
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
		}

		HttpURLConnection conn = null;
		InputStream stream = getStream(url, method, params, conn);
		if (null == stream) {
			throw new RuntimeException("Unable to get stream");
		}

		T object;
		try {
			object = (T) mapper.readValue(stream, valueTypeRef);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (null != conn) {
			conn.disconnect();
		}

		return object;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public <T> T doCall(URL url, JavaType valueType, String method,
			String params) {

		// Read content
		if (null == mapper) {
			mapper = new ObjectMapper();
		}

		// Allow to not check if a field is present in stream but not in
		// class
		if (config.isDisableMapperCheck()) {
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
		}

		HttpURLConnection conn = null;
		InputStream stream = getStream(url, method, params, conn);
		if (null == stream) {
			throw new RuntimeException("Unable to get stream");
		}

		T object;
		try {
			object = (T) mapper.readValue(stream, valueType);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (null != conn) {
			conn.disconnect();
		}

		return object;
	}

	@SuppressWarnings("unused")
	private <T> T doCall(URL url, Class<T> valueType, String method,
			String params) {

		// Read content
		if (null == mapper) {
			mapper = new ObjectMapper();
		}

		// Allow to not check if a field is present in stream but not in
		// class
		if (config.isDisableMapperCheck()) {
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
		}

		HttpURLConnection conn = null;
		InputStream stream = getStream(url, method, params, conn);
		if (null == stream) {
			throw new RuntimeException("Unable to get stream");
		}

		T object;
		try {
			object = (T) mapper.readValue(stream, valueType);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (null != conn) {
			conn.disconnect();
		}

		return object;

	}

	/**
	 * Do a get on the given URL and map the response into the mappedObject type
	 * 
	 * @param url
	 *            The url to get
	 * @param mappedObject
	 *            The response object class
	 * @return
	 */
	public <T> T doGet(URL url, Class<T> mappedObject) {
		return this.<T> doCall(url, mappedObject, "GET", null);
	}

	public <T> T doGet(URL url,
			@SuppressWarnings("rawtypes") TypeReference mappedObject) {
		return this.<T> doCall(url, mappedObject, "GET", null);
	}

	public <T> T doGet(URL url, JavaType mappedObject) {
		return this.<T> doCall(url, mappedObject, "GET", null);
	}

	/**
	 * Do a post on the given URL and map the response into the mappedObject
	 * type
	 * 
	 * @param url
	 *            The url to get
	 * @param mappedObject
	 *            The response object class
	 * @return
	 */
	public <T> T doPost(URL url, String params, Class<T> mappedObject) {
		return this.<T> doCall(url, mappedObject, "POST", params);
	}

	public <T> T doPost(URL url, String params,
			@SuppressWarnings("rawtypes") TypeReference mappedObject) {
		return this.<T> doCall(url, mappedObject, "POST", params);
	}

	public <T> T doPost(URL url, String params, JavaType mappedObject) {
		return this.<T> doCall(url, mappedObject, "POST", params);
	}

	/**
	 * Do a post on the given URL and map the response into the mappedObject
	 * type
	 * 
	 * @param url
	 *            The url to get
	 * @param mappedObject
	 *            The response object class
	 * @return
	 */
	public <T> T doPost(URL url, Object params, Class<T> mappedObject) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return this.<T> doCall(url, mappedObject, "POST",
					mapper.writeValueAsString(params));
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T doPost(URL url, Object params,
			@SuppressWarnings("rawtypes") TypeReference mappedObject) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return this.<T> doCall(url, mappedObject, "POST",
					mapper.writeValueAsString(params));
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T doPost(URL url, Object params, JavaType mappedObject) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return this.<T> doCall(url, mappedObject, "POST",
					mapper.writeValueAsString(params));
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * For test purpose
	 * 
	 * @param inputStream
	 *            the inputStream to set
	 */
	public static void setInputStream(InputStream inputStream) {
		Client.inputStream = inputStream;
	}

	/**
	 * @param mapper
	 *            the mapper to set
	 */
	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

}
