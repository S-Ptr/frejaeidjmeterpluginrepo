package com.internship.frejaeidjmeterplugin.jmeter.sampler;

import com.internship.frejaeidjmeterplugin.jmeter.frejaRequests.AuthenticationService;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResult;
import com.verisec.frejaeid.client.beans.general.RequestedAttributes;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class AuthSampler extends AbstractSampler {

    private final AuthenticationService authService;

    public AuthSampler() throws FrejaEidClientInternalException {
        authService = new AuthenticationService();
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult sr = new SampleResult();
        sr.setTimeStamp(System.currentTimeMillis()/1000L);
        try {
            String reference = authService.initiateAuthenticationRequest("aleksandar.markovic@verisec.com", MinRegistrationLevel.BASIC);
            AuthenticationResult ar = authService.getResults(reference);
            sr.setSuccessful(true);
            sr.setSampleLabel("Freja eID Response: " + ar.getStatus().toString());
            sr.setResponseCode(ar.getStatus().toString());
            sr.setResponseMessage(ar.getStatus() + "");
            if ((ar.getStatus().toString()).equals("APPROVED")) {
                sr.setResponseOK();
                String receivedAuthRef = "AuthRef: " + ar.getAuthRef();
                String details = "Details: " + ar.getDetails();
                RequestedAttributes requestedAttributes = ar.getRequestedAttributes();
                String relyingPartyUserId = "relyingPartyUserId: " + requestedAttributes.getRelyingPartyUserId();
                String dateOfBirth = "Date of Birth: " + requestedAttributes.getDateOfBirth();
                sr.setResponseMessage("{" + receivedAuthRef + " " + relyingPartyUserId + " " + dateOfBirth + " " + details +"}");
            }
        } catch (FrejaEidClientInternalException ex) {
            sr.setSampleLabel("Internal Error");
            sr.setResponseMessage(ex.getClass().getSimpleName());
            Logger.getLogger(AuthSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FrejaEidException ex) {
            sr.setSampleLabel("Freja eID Auth Request Failed");
            sr.setResponseMessage(ex.getClass().getSimpleName());
            Logger.getLogger(AuthSampler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FrejaEidClientPollingException ex) {
            sr.setSuccessful(false);
            sr.setSampleLabel("Freja eID Auth Request Timeout");
            Logger.getLogger(AuthSampler.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            return sr;
        }
    }
}
