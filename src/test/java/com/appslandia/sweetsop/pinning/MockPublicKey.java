package com.appslandia.sweetsop.pinning;

import java.security.PublicKey;

public class MockPublicKey implements PublicKey {
	private static final long serialVersionUID = 1L;

	final byte[] encoded;

	public MockPublicKey(byte[] encoded) {
		this.encoded = encoded;
	}

	@Override
	public byte[] getEncoded() {
		return this.encoded;
	}

	@Override
	public String getAlgorithm() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getFormat() {
		throw new UnsupportedOperationException();
	}
}
