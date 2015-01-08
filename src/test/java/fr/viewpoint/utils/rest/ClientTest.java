package fr.viewpoint.utils.rest;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import fr.viewpoint.utils.rest.Client;
import fr.viewpoint.utils.rest.Config;

public class ClientTest {

	@Test
	public void testGet() throws MalformedURLException {
		Config config = new Config();
		Client client = new Client(config);

		Client.setInputStream(getClass().getResourceAsStream("returnOk.dat"));
		PublishResponse result = client
				.doGet(new URL(
						"http://test/"),
						PublishResponse.class);

		Assert.assertNotNull(result);
		Assert.assertEquals(StatusResponseCode.KO, result.returnCode);
	}

	@Test
	public void testPost() throws MalformedURLException {
		Config config = new Config();
		Client client = new Client(config);

		String params = "{\"date\":1359369244272,\"average\":15.898669915,\"count\":1,\"errorCount\":1,\"min\":0.0,\"max\":15.898669915,\"projectName\":\"WHA\",\"projectKey\":\"UpdateWhaUser\",\"type\":null,\"detail\":null}";

		Client.setInputStream(getClass().getResourceAsStream("returnOk.dat"));
		PublishResponse result = client
				.doPost(new URL(
						"http://test/"),
						(Object) params, PublishResponse.class);

		Assert.assertNotNull(result);
		Assert.assertEquals(StatusResponseCode.KO, result.returnCode);
	}

	public enum StatusResponseCode {
		OK, KO
	}

	private static class PublishResponse {
		public StatusResponseCode returnCode;
		public String message;

		@SuppressWarnings("unused")
		public PublishResponse() {
			super();
		}

		@SuppressWarnings("unused")
		public PublishResponse(StatusResponseCode returnCode, String message) {
			super();
			this.returnCode = returnCode;
			this.message = message;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "PublishResponse [returnCode=" + returnCode + ", message="
					+ message + "]";
		}
	}
}
