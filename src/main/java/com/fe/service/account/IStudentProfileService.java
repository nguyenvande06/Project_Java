package com.fe.service.account;

import com.fe.pojo.StudentProfile;

public interface IStudentProfileService {
	
	StudentProfile getProfile(Long studentId);
    
    void updateInfo(Long studentId, String Mssv);
    
    void linkGithub(Long studentId, String githubUsername);
    
    public void generateOrGetShareToken(Long studentId);
 
    void printProfile(Long studentId);
	

}
