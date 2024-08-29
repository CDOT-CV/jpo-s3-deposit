package us.dot.its.jpo.ode.s3.depositor.imp;

import us.dot.its.jpo.ode.depositor.GeoRoutedMsg;

public class ImpUtil {

    public GeoRoutedMsg getGeoRoutedMsg(String asn1String, String odeReceivedAt, ) {
        
            byte[] asn1 = pojo1.getMetadata().getAsn1().getBytes();
            ByteString asn1ByteString = ByteString.copyFrom(asn1);

            GeoRoutedMsgOrBuilder geoRoutedMsg = GeoRoutedMsg.newBuilder().setMsgBytes(asn1ByteString).set;
    }
}
