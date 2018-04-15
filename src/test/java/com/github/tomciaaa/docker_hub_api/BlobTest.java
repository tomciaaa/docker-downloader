package com.github.tomciaaa.docker_hub_api;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class BlobTest {
    @Test
    public void fetch_busybox_layer() throws IOException, URISyntaxException {
        String imgId = "library/busybox";
        String layerId = "sha256:f70adabe43c0cccffbae8785406d490e26855b8748fc982d14bc2b20c778b929";
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        String auth = //"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsIng1YyI6WyJNSUlDK2pDQ0FwK2dBd0lCQWdJQkFEQUtCZ2dxaGtqT1BRUURBakJHTVVRd1FnWURWUVFERXpzeVYwNVpPbFZMUzFJNlJFMUVVanBTU1U5Rk9reEhOa0U2UTFWWVZEcE5SbFZNT2tZelNFVTZOVkF5VlRwTFNqTkdPa05CTmxrNlNrbEVVVEFlRncweE9EQXlNVFF5TXpBMk5EZGFGdzB4T1RBeU1UUXlNekEyTkRkYU1FWXhSREJDQmdOVkJBTVRPMVpCUTFZNk5VNWFNenBNTkZSWk9sQlFTbGc2VWsxQlZEcEdWalpQT2xZMU1sTTZRa2szV2pwU1REVk9PbGhXVDBJNlFsTmFSanBHVTFRMk1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBMGtyTmgyZWxESnVvYjVERWd5Wi9oZ3l1ZlpxNHo0OXdvNStGRnFRK3VPTGNCMDRyc3N4cnVNdm1aSzJZQ0RSRVRERU9xNW5keEVMMHNaTE51UXRMSlNRdFY1YUhlY2dQVFRkeVJHUTl2aURPWGlqNFBocE40R0N0eFV6YTNKWlNDZC9qbm1YbmtUeDViOElUWXBCZzg2TGNUdmMyRFVUV2tHNy91UThrVjVPNFFxNlZKY05TUWRId1B2Mmp4YWRZa3hBMnhaaWNvRFNFQlpjWGRneUFCRWI2YkRnUzV3QjdtYjRRVXBuM3FXRnRqdCttKzBsdDZOR3hvenNOSFJHd3EwakpqNWtZbWFnWHpEQm5NQ3l5eDFBWFpkMHBNaUlPSjhsaDhRQ09GMStsMkVuV1U1K0thaTZKYVNEOFZJc2VrRzB3YXd4T1dER3U0YzYreE1XYUx3SURBUUFCbzRHeU1JR3ZNQTRHQTFVZER3RUIvd1FFQXdJSGdEQVBCZ05WSFNVRUNEQUdCZ1JWSFNVQU1FUUdBMVVkRGdROUJEdFdRVU5XT2pWT1dqTTZURFJVV1RwUVVFcFlPbEpOUVZRNlJsWTJUenBXTlRKVE9rSkpOMW82VWt3MVRqcFlWazlDT2tKVFdrWTZSbE5VTmpCR0JnTlZIU01FUHpBOWdEc3lWMDVaT2xWTFMxSTZSRTFFVWpwU1NVOUZPa3hITmtFNlExVllWRHBOUmxWTU9rWXpTRVU2TlZBeVZUcExTak5HT2tOQk5sazZTa2xFVVRBS0JnZ3Foa2pPUFFRREFnTkpBREJHQWlFQWdZTWF3Si9uMXM0dDlva0VhRjh2aGVkeURzbERObWNyTHNRNldmWTFmRTRDSVFEbzNWazJXcndiSjNmU1dwZEVjT3hNazZ1ZEFwK2c1Nkd6TjlRSGFNeVZ1QT09Il19";
        Auth.GetAuthToken(imgId).getAccessToken();


        Blob.Fetch(imgId, layerId, auth, os);

        assertThat(os.size()).isEqualTo(723172);

    }
}
