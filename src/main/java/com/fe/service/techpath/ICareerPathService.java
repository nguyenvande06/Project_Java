package com.fe.service.techpath;

public interface ICareerPathService {

    void selectAndSetTargetPath(Long studentId, int choice);

    void executeSkillGapAnalysis(Long studentId);

    void printFullSkillTree(Long studentId);
}