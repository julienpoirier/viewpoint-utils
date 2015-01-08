package fr.viewpoint.utils.rest;

/**
 * Manage Client options
 * 
 * @author jpoirier
 * 
 */
public class Config {

	private String proxyHost;
	private Integer proxyPort;
	private String basicAuthLogin;
	private String basicAuthPassword;
	private boolean disableMapperCheck = false;
	private Integer timeout = null;

	/**
	 * Has basic auth configuration
	 * 
	 * @return
	 */
	public boolean hasProxy() {
		return (null != proxyHost && null != proxyPort && !"".equals(proxyHost) && !""
				.equals(proxyPort));
	}

	/**
	 * @param proxyPort
	 *            the proxyPort to set
	 */
	public void setProxy(String proxyHost, Integer proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	/**
	 * Has basic auth configuration
	 * 
	 * @return
	 */
	public boolean hasBasicAuth() {
		return (null != basicAuthLogin && null != basicAuthPassword
				&& !"".equals(basicAuthPassword) && !"".equals(basicAuthLogin));
	}

	/**
	 * @param basicAuthLogin
	 *            the basicAuthLogin to set
	 */
	public void setBasicAuthLogin(String basicAuthLogin,
			String basicAuthPassword) {
		this.basicAuthLogin = basicAuthLogin;
		this.basicAuthPassword = basicAuthPassword;
	}

	/**
	 * @param disableMapperCheck
	 *            the disableMapperCheck to set
	 */
	public void setDisableMapperCheck(boolean disableMapperCheck) {
		this.disableMapperCheck = disableMapperCheck;
	}

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public Integer getProxyPort() {
		return proxyPort;
	}

	/**
	 * @return the basicAuthLogin
	 */
	public String getBasicAuthLogin() {
		return basicAuthLogin;
	}

	/**
	 * @return the basicAuthPassword
	 */
	public String getBasicAuthPassword() {
		return basicAuthPassword;
	}

	/**
	 * @return the disableMapperCheck
	 */
	public boolean isDisableMapperCheck() {
		return disableMapperCheck;
	}

	/**
	 * @return the timeout
	 */
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((basicAuthLogin == null) ? 0 : basicAuthLogin.hashCode());
		result = prime
				* result
				+ ((basicAuthPassword == null) ? 0 : basicAuthPassword
						.hashCode());
		result = prime * result + (disableMapperCheck ? 1231 : 1237);
		result = prime * result
				+ ((proxyHost == null) ? 0 : proxyHost.hashCode());
		result = prime * result
				+ ((proxyPort == null) ? 0 : proxyPort.hashCode());
		result = prime * result + ((timeout == null) ? 0 : timeout.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Config other = (Config) obj;
		if (basicAuthLogin == null) {
			if (other.basicAuthLogin != null)
				return false;
		} else if (!basicAuthLogin.equals(other.basicAuthLogin))
			return false;
		if (basicAuthPassword == null) {
			if (other.basicAuthPassword != null)
				return false;
		} else if (!basicAuthPassword.equals(other.basicAuthPassword))
			return false;
		if (disableMapperCheck != other.disableMapperCheck)
			return false;
		if (proxyHost == null) {
			if (other.proxyHost != null)
				return false;
		} else if (!proxyHost.equals(other.proxyHost))
			return false;
		if (proxyPort == null) {
			if (other.proxyPort != null)
				return false;
		} else if (!proxyPort.equals(other.proxyPort))
			return false;
		if (timeout == null) {
			if (other.timeout != null)
				return false;
		} else if (!timeout.equals(other.timeout))
			return false;
		return true;
	}

}
