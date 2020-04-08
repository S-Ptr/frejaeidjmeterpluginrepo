package com.internship.frejaeidjmeterplugin.jmeter.sampler;

import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class FrejaEidPluginSampler extends AbstractSampler {

    private final AuthSampler authSampler;
    private final SignSampler signSampler;
    private final MobileClientSampler mobileClientSampler;
    private final HashMap<String, GenericSampler> samplerMap;
    private String[] requests;

    public FrejaEidPluginSampler() throws FrejaEidClientInternalException, Exception {
        authSampler = new AuthSampler();
        signSampler = new SignSampler();
        mobileClientSampler = new MobileClientSampler();
        samplerMap = new HashMap<>();
        addSamplers();
    }

    public String getEmail() {
        return getPropertyAsString("email");
    }

    public void setEmail(String email) {
        setProperty("email", email);
    }

    public void setCheckAuth(boolean isChecked) {
        setProperty("auth", isChecked);
    }

    public void setCheckSign(boolean isChecked) {
        setProperty("sign", isChecked);
    }

    public void setCheckOpenSecureConnection(boolean isChecked) {
        setProperty("opse", isChecked);
    }

    public void setRequests(String requests) {
        setProperty("requests", requests);
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult sampleResult = new SampleResult();
        requests = getProperty("requests").toString().split(" ");
        switch (requests.length) {
            case RequestNumber.NO_REQUEST:
                sampleResult.setSampleLabel("noAction");
                break;
            case RequestNumber.ONE_REQUEST:
                GenericSampler currentSampler = samplerMap.get(requests[0]);
                sampleResult = currentSampler.sample(getEmail());
                break;
            default:
                sampleResult = processAllRequests();
        }
        return sampleResult;
    }

    private byte[] getDataAsByteArray(HashMap<String, String> response) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(response);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(FrejaEidPluginSampler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                baos.close();
            } catch (IOException ex) {
                Logger.getLogger(FrejaEidPluginSampler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return baos.toByteArray();
    }

    private void addSamplers() {
        samplerMap.put("auth", authSampler);
        samplerMap.put("sign", signSampler);
        samplerMap.put("mobile", mobileClientSampler);
    }

    private SampleResult processAllRequests() {
        SampleResult sampleResult = new SampleResult();
        HashMap<String, String> response = new HashMap<>();
        for (String request : requests) {
            GenericSampler genericSampler = samplerMap.get(request);
            SampleResult currentSamplerResult = genericSampler.sample(getEmail());
            response.put(genericSampler.getSamplerName(), currentSamplerResult.getResponseCode());
        }
        sampleResult.setResponseData(getDataAsByteArray(response));
        return sampleResult;
    }
}
