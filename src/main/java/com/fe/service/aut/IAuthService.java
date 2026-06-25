package com.fe.service.aut;

import com.fe.pojo.User;

public interface IAuthService {
	
    boolean registerLocal(String fullName, String email, String password);
    
    boolean registerGoogleMock(String fullName, String email);
    
    User loginLocal(String email, String password);
    
    User loginGoogleMock(String email);
}
