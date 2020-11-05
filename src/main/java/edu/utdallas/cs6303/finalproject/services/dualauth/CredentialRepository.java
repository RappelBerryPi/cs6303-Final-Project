package edu.utdallas.cs6303.finalproject.services.dualauth;

import java.util.List;

import com.warrenstrange.googleauth.ICredentialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.UserTOTP;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserTOTPRepository;

@Service
public class CredentialRepository implements ICredentialRepository {

    @Autowired
    private UserTOTPRepository userTOTPRepository;

    @Override
    public String getSecretKey(String userName) {
        return userTOTPRepository.findFirstByUserName(userName).getSecretKey();
    }

    @Override
    public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
        UserTOTP totp = new UserTOTP();
        totp.setUserName(userName);
        totp.setSecretKey(secretKey);
        totp.setValidationCode(validationCode);
        totp.setScratchCodes(scratchCodes);
        userTOTPRepository.save(totp);
    }
}