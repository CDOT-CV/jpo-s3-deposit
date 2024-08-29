package us.dot.its.jpo.ode.s3.depositor.pojos.imp;

import java.util.Objects;

public class ImpConfigData {

    private String clientType;
    private String clientSubType;
    private String networkType;
    private String deviceFixedLocationLat;
    private String deviceFixedLocationLong;
    private String outputDirectory;
    private String caCert;
    private String clientCert;
    private String keyFile;
    private String impIpAddress;
    private int impPort;
    private String deviceID;

    // Generated using vscode plugin: sohibe.java-generate-setters-getters
    // action: generate everything

    public ImpConfigData() {
    }

    public ImpConfigData(String clientType, String clientSubType, String networkType, String deviceFixedLocationLat,
            String deviceFixedLocationLong, String outputDirectory, String caCert, String clientCert, String keyFile,
            String impIpAddress, int impPort, String deviceID) {
        this.clientType = clientType;
        this.clientSubType = clientSubType;
        this.networkType = networkType;
        this.deviceFixedLocationLat = deviceFixedLocationLat;
        this.deviceFixedLocationLong = deviceFixedLocationLong;
        this.outputDirectory = outputDirectory;
        this.caCert = caCert;
        this.clientCert = clientCert;
        this.keyFile = keyFile;
        this.impIpAddress = impIpAddress;
        this.impPort = impPort;
        this.deviceID = deviceID;
    }

    public String getClientType() {
        return this.clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientSubType() {
        return this.clientSubType;
    }

    public void setClientSubType(String clientSubType) {
        this.clientSubType = clientSubType;
    }

    public String getNetworkType() {
        return this.networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getDeviceFixedLocationLat() {
        return this.deviceFixedLocationLat;
    }

    public void setDeviceFixedLocationLat(String deviceFixedLocationLat) {
        this.deviceFixedLocationLat = deviceFixedLocationLat;
    }

    public String getDeviceFixedLocationLong() {
        return this.deviceFixedLocationLong;
    }

    public void setDeviceFixedLocationLong(String deviceFixedLocationLong) {
        this.deviceFixedLocationLong = deviceFixedLocationLong;
    }

    public String getOutputDirectory() {
        return this.outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getCaCert() {
        return this.caCert;
    }

    public void setCaCert(String caCert) {
        this.caCert = caCert;
    }

    public String getClientCert() {
        return this.clientCert;
    }

    public void setClientCert(String clientCert) {
        this.clientCert = clientCert;
    }

    public String getKeyFile() {
        return this.keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public String getImpIpAddress() {
        return this.impIpAddress;
    }

    public void setImpIpAddress(String impIpAddress) {
        this.impIpAddress = impIpAddress;
    }

    public int getImpPort() {
        return this.impPort;
    }

    public void setImpPort(int impPort) {
        this.impPort = impPort;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public ImpConfigData clientType(String clientType) {
        setClientType(clientType);
        return this;
    }

    public ImpConfigData clientSubType(String clientSubType) {
        setClientSubType(clientSubType);
        return this;
    }

    public ImpConfigData networkType(String networkType) {
        setNetworkType(networkType);
        return this;
    }

    public ImpConfigData deviceFixedLocationLat(String deviceFixedLocationLat) {
        setDeviceFixedLocationLat(deviceFixedLocationLat);
        return this;
    }

    public ImpConfigData deviceFixedLocationLong(String deviceFixedLocationLong) {
        setDeviceFixedLocationLong(deviceFixedLocationLong);
        return this;
    }

    public ImpConfigData outputDirectory(String outputDirectory) {
        setOutputDirectory(outputDirectory);
        return this;
    }

    public ImpConfigData caCert(String caCert) {
        setCaCert(caCert);
        return this;
    }

    public ImpConfigData clientCert(String clientCert) {
        setClientCert(clientCert);
        return this;
    }

    public ImpConfigData keyFile(String keyFile) {
        setKeyFile(keyFile);
        return this;
    }

    public ImpConfigData impIpAddress(String impIpAddress) {
        setImpIpAddress(impIpAddress);
        return this;
    }

    public ImpConfigData impPort(int impPort) {
        setImpPort(impPort);
        return this;
    }

    public ImpConfigData deviceID(String deviceID) {
        setDeviceID(deviceID);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ImpConfigData)) {
            return false;
        }
        ImpConfigData impConfigData = (ImpConfigData) o;
        return Objects.equals(clientType, impConfigData.clientType)
                && Objects.equals(clientSubType, impConfigData.clientSubType)
                && Objects.equals(networkType, impConfigData.networkType)
                && Objects.equals(deviceFixedLocationLat, impConfigData.deviceFixedLocationLat)
                && Objects.equals(deviceFixedLocationLong, impConfigData.deviceFixedLocationLong)
                && Objects.equals(outputDirectory, impConfigData.outputDirectory)
                && Objects.equals(caCert, impConfigData.caCert) && Objects.equals(clientCert, impConfigData.clientCert)
                && Objects.equals(keyFile, impConfigData.keyFile)
                && Objects.equals(impIpAddress, impConfigData.impIpAddress) && impPort == impConfigData.impPort
                && Objects.equals(deviceID, impConfigData.deviceID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientType, clientSubType, networkType, deviceFixedLocationLat, deviceFixedLocationLong,
                outputDirectory, caCert, clientCert, keyFile, impIpAddress, impPort, deviceID);
    }

    @Override
    public String toString() {
        return "{" + " clientType='" + getClientType() + "'" + ", clientSubType='" + getClientSubType() + "'"
                + ", networkType='" + getNetworkType() + "'" + ", deviceFixedLocationLat='"
                + getDeviceFixedLocationLat() + "'" + ", deviceFixedLocationLong='" + getDeviceFixedLocationLong() + "'"
                + ", outputDirectory='" + getOutputDirectory() + "'" + ", caCert='" + getCaCert() + "'"
                + ", clientCert='" + getClientCert() + "'" + ", keyFile='" + getKeyFile() + "'" + ", impIpAddress='"
                + getImpIpAddress() + "'" + ", impPort='" + getImpPort() + "'" + ", deviceID='" + getDeviceID() + "'"
                + "}";
    }

}