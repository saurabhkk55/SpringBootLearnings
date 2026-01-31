package DP.ObserverDesignPattern.GitHub;

import java.util.Set;

public interface Repository {
    void follow(Developer developer);
    void unfollow(Developer developer);

    void notifyDevelopers(String commitHash, String commitByDeveloperName);

    String getDevelopers();
    String getRepositoryName();

    void getRepoMetaData();
}
