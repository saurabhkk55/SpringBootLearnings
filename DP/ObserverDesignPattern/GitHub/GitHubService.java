package DP.ObserverDesignPattern.GitHub;

import DP.ObserverDesignPattern.GitHub.impl.DeveloperImpl;
import DP.ObserverDesignPattern.GitHub.impl.RepositoryImpl;

public class GitHubService {
    static void main() {
        Repository javaRepo = new RepositoryImpl("JavaRepository");
        Repository rustRepo = new RepositoryImpl("RustRepository");

        Developer amy = new DeveloperImpl("Amy");
        Developer baccy = new DeveloperImpl("Baccy");
        Developer cathy = new DeveloperImpl("Cathy");

        amy.followRepository(javaRepo);
        baccy.followRepository(javaRepo);
        cathy.followRepository(rustRepo);

        System.out.println(javaRepo.getDevelopers());
        System.out.println(rustRepo.getDevelopers());

        System.out.println("=======================================");

        amy.makeCommit("commit@1", javaRepo);

        System.out.println("=======================================");

        cathy.makeCommit("commit@11", rustRepo);

        System.out.println("=======================================");

//        cathy.makeCommit("commit@11", javaRepo); // throws exception as an expected behaviour because cathy is not a developer for javaRepo so far.

        System.out.println("=======================================");

        cathy.followRepository(javaRepo);
        System.out.println(javaRepo.getDevelopers());

        System.out.println("=======================================");

        cathy.makeCommit("commit@2", javaRepo);

        System.out.println("=======================================");

        cathy.makeCommit("commit@3", rustRepo);

        System.out.println("=======================================");

        baccy.followRepository(rustRepo);
        System.out.println(rustRepo.getDevelopers());

        System.out.println("=======================================");

        baccy.makeCommit("commit@4", rustRepo);
        baccy.makeCommit("commit@4", rustRepo);

        System.out.println("=======================================");

        baccy.makeCommit("commit@5", javaRepo);

        System.out.println("=======================================");

        amy.getMyRepos();
        baccy.getMyRepos();
        cathy.getMyRepos();

        System.out.println("=======================================");

        javaRepo.getRepoMetaData();
        rustRepo.getRepoMetaData();

        System.out.println("=======================================");

        amy.unFollowRepository(rustRepo);
        amy.unFollowRepository(javaRepo);

        System.out.println("=======================================");

        javaRepo.getRepoMetaData();
        rustRepo.getRepoMetaData();

        System.out.println("=======================================");

        Repository randomRepo = new RepositoryImpl("RandomRepo");

        System.out.println("=======================================");

        javaRepo.getRepoMetaData();
        rustRepo.getRepoMetaData();
        randomRepo.getRepoMetaData();

        System.out.println("=======================================");

        baccy.makeCommit("commit@9", javaRepo);
        baccy.makeCommit("commit@9", rustRepo);

        System.out.println("=======================================");

        baccy.unFollowRepository(randomRepo);
    }
}

// ============ OUTPUT ============
//JavaRepository's developers [Baccy, Amy]
//RustRepository's developers [Cathy]
//=======================================
//Hi Baccy, a new commit commit@1 is made by Amy in the repo JavaRepository
//Hi Amy, a new commit commit@1 is made by Amy in the repo JavaRepository
//=======================================
//Hi Cathy, a new commit commit@11 is made by Cathy in the repo RustRepository
//=======================================
//=======================================
//JavaRepository's developers [Baccy, Cathy, Amy]
//=======================================
//Hi Baccy, a new commit commit@2 is made by Cathy in the repo JavaRepository
//Hi Cathy, a new commit commit@2 is made by Cathy in the repo JavaRepository
//Hi Amy, a new commit commit@2 is made by Cathy in the repo JavaRepository
//=======================================
//Hi Cathy, a new commit commit@3 is made by Cathy in the repo RustRepository
//=======================================
//RustRepository's developers [Cathy, Baccy]
//=======================================
//Hi Cathy, a new commit commit@4 is made by Baccy in the repo RustRepository
//Hi Baccy, a new commit commit@4 is made by Baccy in the repo RustRepository
//commit@4 is a duplicate commit-hash, no need to notify any developer(s) for this.
//=======================================
//Hi Baccy, a new commit commit@5 is made by Baccy in the repo JavaRepository
//Hi Cathy, a new commit commit@5 is made by Baccy in the repo JavaRepository
//Hi Amy, a new commit commit@5 is made by Baccy in the repo JavaRepository
//=======================================
//Amy follows JavaRepository
//Baccy follows JavaRepository, RustRepository
//Cathy follows JavaRepository, RustRepository
//=======================================
//JavaRepository => Developers[Baccy, Cathy, Amy]
//JavaRepository => Commits[commit@2, commit@1, commit@5]
//RustRepository => Developers[Cathy, Baccy]
//RustRepository => Commits[commit@4, commit@3, commit@11]
//=======================================
//Amy doesn't follow RustRepository
//Amy unfollowed JavaRepository successfully
//=======================================
//JavaRepository => Developers[Baccy, Cathy]
//JavaRepository => Commits[commit@2, commit@1, commit@5]
//RustRepository => Developers[Cathy, Baccy]
//RustRepository => Commits[commit@4, commit@3, commit@11]
//=======================================
//=======================================
//JavaRepository => Developers[Baccy, Cathy]
//JavaRepository => Commits[commit@2, commit@1, commit@5]
//RustRepository => Developers[Cathy, Baccy]
//RustRepository => Commits[commit@4, commit@3, commit@11]
//=======================================
//Hi Baccy, a new commit commit@9 is made by Baccy in the repo JavaRepository
//Hi Cathy, a new commit commit@9 is made by Baccy in the repo JavaRepository
//Hi Cathy, a new commit commit@9 is made by Baccy in the repo RustRepository
//Hi Baccy, a new commit commit@9 is made by Baccy in the repo RustRepository
//Exception in thread "main" java.lang.RuntimeException: RandomRepo has no developers
//at DP.ObserverDesignPattern.GitHub.impl.RepositoryImpl.unfollow(RepositoryImpl.java:28)
//at DP.ObserverDesignPattern.GitHub.impl.DeveloperImpl.unFollowRepository(DeveloperImpl.java:32)
//at DP.ObserverDesignPattern.GitHub.GitHubService.main(GitHubService.java:96)
