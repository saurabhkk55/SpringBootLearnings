package DP.ObserverDesignPattern.GitHub.impl;

import java.util.*;
import java.util.stream.Collectors;

import DP.ObserverDesignPattern.GitHub.Developer;
import DP.ObserverDesignPattern.GitHub.Repository;

public class RepositoryImpl implements Repository {
    Set<Developer> developers;
    Set<String> commits;
    String repositoryName;
    Map<Repository, Set<String>> repoCommits;
    Map<Repository, Set<Developer>> repoDevelopers;

    public RepositoryImpl(String repositoryName) {
        this.developers = new HashSet<>();
        this.commits = new HashSet<>();
        this.repoCommits = new HashMap<>();
        this.repoDevelopers = new HashMap<>();
        this.repositoryName = repositoryName;
    }

    @Override
    public void unfollow(Developer developer) {
        boolean isRepoAvailable = repoDevelopers.containsKey(this);
        if(!isRepoAvailable) {
            throw new RuntimeException(repositoryName + " has no developers");
        }
        boolean isDeveloperRemoved = repoDevelopers.get(this).remove(developer);
        if(isDeveloperRemoved) System.out.println(developer.getDeveloperName() + " unfollowed " + repositoryName + " successfully");
        else System.out.println(developer.getDeveloperName() + " doesn't follow " + repositoryName);
    }

    @Override
    public void follow(Developer developer) {
        developers.add(developer);
        repoDevelopers.putIfAbsent(this, developers);
    }

    @Override
    public void notifyDevelopers(String commitHash, String commitByDeveloperName) {
        if(Objects.isNull(commitHash)) {
            throw new RuntimeException("Invalid commit " + commitHash);
        }

        boolean isCommitAdded = commits.add(commitHash);

        if(!isCommitAdded) {
            System.out.println(commitHash + " is a duplicate commit-hash, no need to notify any developer(s) for this.");
            return;
        }

        repoCommits.putIfAbsent(this, commits);
        repoDevelopers.get(this).forEach(developer -> developer.update(commitHash, commitByDeveloperName, repositoryName));
    }

    @Override
    public String getDevelopers() {
        String developersNames = repoDevelopers.get(this).stream()
                .map(Developer::getDeveloperName)
                .collect(Collectors.joining(", "));

        return repositoryName + "'s developers [" + developersNames + "]";
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    @Override
    public void getRepoMetaData() {
        repoDevelopers.forEach((k, v) -> {
            List<String> devs = v.stream()
                    .map(Developer::getDeveloperName)
                    .toList();
            System.out.println(k.getRepositoryName() + " => Developers" + devs);
        });

        repoCommits.forEach((k, v) -> {
            List<String> commits = v.stream().toList();
            System.out.println(k.getRepositoryName() + " => Commits" + commits);
        });
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
