package com.appslandia.sweetsop.pinning;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

public class MockX509Certificate extends X509Certificate {

	private byte[] encoded;
	private MockPublicKey publicKey;

	public MockX509Certificate(byte[] encoded) {
		this.encoded = encoded;
	}

	public MockX509Certificate(MockPublicKey publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	@Override
	public byte[] getEncoded() throws CertificateEncodingException {
		return this.encoded;
	}

	@Override
	public boolean hasUnsupportedCriticalExtension() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getCriticalExtensionOIDs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getNonCriticalExtensionOIDs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getExtensionValue(String oid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BigInteger getSerialNumber() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getIssuerDN() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getSubjectDN() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getNotBefore() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getNotAfter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getTBSCertificate() throws CertificateEncodingException {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getSignature() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSigAlgName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSigAlgOID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getSigAlgParams() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean[] getIssuerUniqueID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean[] getSubjectUniqueID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean[] getKeyUsage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getBasicConstraints() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}
