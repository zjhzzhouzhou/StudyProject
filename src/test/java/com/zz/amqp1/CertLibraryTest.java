package com.zz.amqp1;

import com.zz.amqp1.utils.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

/**
 * Description:
 * User: zhouzhou
 * Date: 2018-09-05
 * Time: 13:36
 */
public class CertLibraryTest {

    public static char[] password = "changeit".toCharArray();
    public static String type = "pkcs12";
    // -------------------------------------------------- 证书库测试 --------------------------------------------------------

    /**
     * 写证书库
     * @throws Exception
     */
    @Test
    public void testGetCert() throws Exception {
        String btye64 =
                "MIIDjDCCAy+gAwIBAgIMXY0AAAABKLLWdnJLMAwGCCqBHM9VAYN1BQAwRDELMAkGA1UEBhMCQ04xDzANBgNVBAoMBkdBU0tleTEWMBQGA1UEAwwNU0tNUCBTQ1NMUEtTUzEMMAoGA1UEBQwDMDAxMB4XDTE4MDkzMDA3MzMyM1oXDTIxMDkyOTA3MzMyM1owQTEeMBwGA1UECgwVNTAgMzYwNDI4MTk5NTExMjkxNDU0MRIwEAYDVQQDDAnkvZnlkI3mtYsxCzAJBgNVBAYTAkNOMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEx+5PMraMlRoPtI4O7rr19lHdHBr5B7nltFuwBS3XAQefjK0R86OCAgYwggICMAwGA1UdEwQFMAMBAQAwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMEMAsGA1UdDwQEAwIAwDARBglghkgBhvhCAQEEBAMCAIAwRgYIKwYBBQUHAQEEOjA4MDYGCCsGAQUFBzAChipodHRwOi8vc2ttcC5nYXNrZXkuY24vY2VydHMvU0tNUFNDUm9vdC5jZXIwPgYDVR0gBDcwNTAzBggqgRyG8BUBATAnMCUGCCsGAQUFBwIBFhlodHRwOi8vc2ttcC5nYXNrZXkuY24vY3BzMBcGAyoDBwQQMDAwMzAwMDAwMDAwMDRDQjAJBgMqAwkEAjEwMCMGAyoDCgQc5L2Z5ZCN5rWLIDM2MDQyODE5OTUxMTI5MTQ1NDAfBgNVHSMEGDAWgBTUgP1DLOjyI+qW5wXrtXBI+nvh0zBQBgNVHR8ESTBHMEWgQ6BBhj9odHRwOi8vc2ttcC5nYXNrZXkuY24vY3Jscy8wMDEvU00yX1NLTVAlMjBTQ1NMUEtTU19ncm91cDBfYi5jcmwwUAYDVR0uBEkwRzBFoEOgQYY/aHR0cDovL3NrbXAuZ2Fza2V5LmNuL2NybHMvMDAxL1NNMl9TS01QJTIwU0NTTFBLU1NfZ3JvdXAwX2QuY3JsMB0GA1UdDgQWBBSk+hvxlwN3s5E6vLhU0ilzJ8RsjjAMBggqgRzPVQGDdQUAA0kAMEYCIQCHclkUvyQAd+ZM96SfMdmNanOEouczizsE9CF/YfRQqgIhAOtCef7nnpYiFtEqtnocSPgSds3M+wQBwrJoKE83BzPz";
        byte[] bytes = Base64Utils.decodeFromString(btye64);
        InputStream input = new ByteArrayInputStream(bytes);
        Security.addProvider(new BouncyCastleProvider());

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
        X509Certificate x509certificate = (X509Certificate) certificateFactory.generateCertificate(input);

        char[] password = "changeit".toCharArray();
        // 拿到证书后进行封装
        KeyStore outputKeyStore = KeyStore.getInstance(type);
        outputKeyStore.load(null, password);
        outputKeyStore.setCertificateEntry("测试证书一号", x509certificate);

        boolean file = FileUtils.createFile("cert/测试证书一号." + type);
        OutputStream keyStoreOutputStream = new FileOutputStream("cert/测试证书一号." + type);

        outputKeyStore.store(keyStoreOutputStream, password);
        keyStoreOutputStream.flush();
        keyStoreOutputStream.close();

    }

    // 读证书库文件
    @Test
    public void parseCertbks() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        char[] password = "changeit".toCharArray();
        InputStream keyStoreInputStream = new FileInputStream("C:\\Users\\x4\\Desktop\\rootCertificate\\v000105.p12");
        // 读流
        readCertStream(password, keyStoreInputStream);
    }


    /**
     * 证书库转64位字符串并解析
     * @throws Exception
     */
    @Test
    public void testStreamTo64Str() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        //InputStream keyStoreInputStream = new FileInputStream("C:\\Users\\x4\\Desktop\\rootCertificate\\test\\cacerts-gm.bks");
        InputStream keyStoreInputStream = new FileInputStream("E:\\project\\project0716\\dzzd-plat\\rootcertfiles\\v000010.p12");
        byte[] bytes = FileUtils.toByteArray(keyStoreInputStream);
        String byte64 = Base64Utils.encodeToString(bytes);
        System.out.println("64编码的流:" + byte64);
        System.out.println("----------------------开始解析编码------------------------");
        byte[] bys = Base64Utils.decodeFromString(byte64);
        InputStream is = new ByteArrayInputStream(bys);
        readCertStream(password,is);

    }

    /**
     * 读64位证书库字符串
     * @throws Exception
     */
    @Test
    public void readLibrary() throws Exception{
        Security.addProvider(new BouncyCastleProvider());
        StringBuffer base64 =new StringBuffer("");
        byte[] bys = Base64Utils.decodeFromString(base64.toString());
        byte[] decode = Base64.getDecoder().decode(base64.toString());
        InputStream is = new ByteArrayInputStream(bys);
        readCertStream(password,is);
    }

    @Test
    public void readCert() throws Exception{
        Security.addProvider(new BouncyCastleProvider());
        byte[] certs = Base64.getDecoder().decode(

                "MIIDjDCCAy+gAwIBAgIMXY0AAAABKLLWdnJLMAwGCCqBHM9VAYN1BQAwRDELMAkGA1UEBhMCQ04xDzANBgNVBAoMBkdBU0tleTEWMBQGA1UEAwwNU0tNUCBTQ1NMUEtTUzEMMAoGA1UEBQwDMDAxMB4XDTE4MDkzMDA3MzMyM1oXDTIxMDkyOTA3MzMyM1owQTEeMBwGA1UECgwVNTAgMzYwNDI4MTk5NTExMjkxNDU0MRIwEAYDVQQDDAnkvZnlkI3mtYsxCzAJBgNVBAYTAkNOMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEx+5PMraMlRoPtI4O7rr19lHdHBr5B7nltFuwBS3XAQefjK0R86OCAgYwggICMAwGA1UdEwQFMAMBAQAwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMEMAsGA1UdDwQEAwIAwDARBglghkgBhvhCAQEEBAMCAIAwRgYIKwYBBQUHAQEEOjA4MDYGCCsGAQUFBzAChipodHRwOi8vc2ttcC5nYXNrZXkuY24vY2VydHMvU0tNUFNDUm9vdC5jZXIwPgYDVR0gBDcwNTAzBggqgRyG8BUBATAnMCUGCCsGAQUFBwIBFhlodHRwOi8vc2ttcC5nYXNrZXkuY24vY3BzMBcGAyoDBwQQMDAwMzAwMDAwMDAwMDRDQjAJBgMqAwkEAjEwMCMGAyoDCgQc5L2Z5ZCN5rWLIDM2MDQyODE5OTUxMTI5MTQ1NDAfBgNVHSMEGDAWgBTUgP1DLOjyI+qW5wXrtXBI+nvh0zBQBgNVHR8ESTBHMEWgQ6BBhj9odHRwOi8vc2ttcC5nYXNrZXkuY24vY3Jscy8wMDEvU00yX1NLTVAlMjBTQ1NMUEtTU19ncm91cDBfYi5jcmwwUAYDVR0uBEkwRzBFoEOgQYY/aHR0cDovL3NrbXAuZ2Fza2V5LmNuL2NybHMvMDAxL1NNMl9TS01QJTIwU0NTTFBLU1NfZ3JvdXAwX2QuY3JsMB0GA1UdDgQWBBSk+hvxlwN3s5E6vLhU0ilzJ8RsjjAMBggqgRzPVQGDdQUAA0kAMEYCIQCHclkUvyQAd+ZM96SfMdmNanOEouczizsE9CF/YfRQqgIhAOtCef7nnpYiFtEqtnocSPgSds3M+wQBwrJoKE83BzPz"

        );
        InputStream input = new ByteArrayInputStream(certs);
        // 获取证书的详情
        CertificateFactory certificate_factory = CertificateFactory.getInstance("X.509", "BC");
        X509Certificate x509certificate = (X509Certificate) certificate_factory.generateCertificate(input);
        input.close();
        System.out.println(String.valueOf(x509certificate.getVersion()));
        System.out.println(x509certificate.getSerialNumber().toString(16));
    }


    // 读证书流
    private void readCertStream(char[] password, InputStream keyStoreInputStream) throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore inputKeyStore = KeyStore.getInstance(type, "BC");
        inputKeyStore.load(keyStoreInputStream, password);
        // 读取证书
        Enumeration<String> enumeration = inputKeyStore.aliases();
        while (enumeration.hasMoreElements()) {
            String alisa = enumeration.nextElement();
            X509Certificate certificate = (X509Certificate) inputKeyStore.getCertificate(alisa);

            System.out.println("版本:" + certificate.getVersion());
            System.out.println("序列号:" + certificate.getSerialNumber());
            System.out.println("公钥:" + certificate.getPublicKey());
            System.out.println("颁发者:" + certificate.getIssuerDN().getName());
        }
    }
}
