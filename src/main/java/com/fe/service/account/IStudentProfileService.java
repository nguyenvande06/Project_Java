package com.fe.service.account;

import com.fe.pojo.StudentProfile;

public interface IStudentProfileService {
	
	StudentProfile getProfile(Long studentId);
    
    void updateInfo(Long studentId, String newFullName, String newMssv);
    
    void linkGithub(Long studentId, String githubUsername);
    
    String generateOrGetShareToken(Long studentId);
 
    void printProfile(Long studentId);
	

}
