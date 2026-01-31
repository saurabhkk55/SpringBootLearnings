package DP.ObserverDesignPattern.GitHub.impl;

import DP.ObserverDesignPattern.GitHub.Developer;
import DP.ObserverDesignPattern.GitHub.Repository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DeveloperImpl implements Developer {
    String developerName;
    Set<Repository> repositories;

    public DeveloperImpl(String developerName) {
        this.developerName = developerName;
        this.repositories = new HashSet<>();
    }

    @Override
    public void followRepository(Repository repository) {
        repositories.add(repository);
        repository.follow(this); // this represents a developer object
    }

    @Override
    public void unFollowRepository(Repository repository) {
        if(Objects.isNull(repository)) {
            throw new RuntimeException("Repository cannot be null");
        }

        repository.unfollow(this); // this represents a developer object
    }

    @Override
    public void createIssue() {

    }

    @Override
    public void createPullRequest() {

    }

    @Override
    public void makeCommit(String commitHash, Repository repository) {
        if(!repositories.contains(repository)) {
            throw new RuntimeException("[ERROR] " + developerName + " has no repository named " + repository + " (" + repository.getRepositoryName() + ")");
        }
        repository.notifyDevelopers(commitHash, developerName);
    }

    @Override
    public void update(String commitHash, String commitByDeveloperName, String repoInWhichCommitIsMade) {
        String msg = "Hi " + developerName + ", a new commit " + commitHash + " is made by " + commitByDeveloperName + " in the repo " + repoInWhichCommitIsMade;
        System.out.println(msg);
    }

    @Override
    public String getDeveloperName() {
        return developerName;
    }

    @Override
    public void getMyRepos() {
        String repos = repositories.stream()
                .map(Repository::getRepositoryName)
                .collect(Collectors.joining(", "));

        System.out.println(developerName + " follows " + repos);
    }
}
