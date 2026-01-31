package DP.ObserverDesignPattern.GitHub;

public interface Developer {
    void followRepository(Repository repository);
    void unFollowRepository(Repository repository);
    void createIssue();
    void createPullRequest();
    void makeCommit(String commitHash, Repository repository);
    void update(String commitHash, String commitByDeveloperName, String repoInWhichCommitIsMade);
    String getDeveloperName();
    void getMyRepos();
}
