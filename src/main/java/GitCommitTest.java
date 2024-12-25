import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GitCommitTest {

	public static void main(String[] args) {
		String path = "D:/FileCommits"; // Path to your repository

		GitCommitTest gc = new GitCommitTest();
		gc.gitCommit(path);
	}

	public void gitCommit(String path) {
		try {
			System.out.println("Git Commit started!");

			// Path to the Git repository
			Path repoPath = Paths.get(path);
			File repoDir = repoPath.toFile();

			// Open existing repository if exists, else initialize it
			Git git;

			git = Git.init().setDirectory(repoDir).call(); // Initialize a new repo
			System.out.println("New repository initialized.");

			// Check if the file exists in the repository directory
			// File file = new File(repoPath.toFile(), "example_01.txt"); // Ensure this
			// path is correct

			/*
			 * if (file.exists()) { System.out.println("File exists."); // Stage the
			 * specific file git.add().addFilepattern(file.getName()).call(); } else {
			 * System.out.println("File does not exist."); return; // Exit if the file
			 * doesn't exist }
			 */
			// Check the status to ensure staging has occurred
			Status status = git.status().call();
			System.out.println("status:" + status);
			// Adding unstaged files
			Set<String> unstagedfiles = new HashSet<>();
			unstagedfiles.addAll(status.getUntracked());
			unstagedfiles.addAll(status.getModified());
			System.out.println("unstaged files" +unstagedfiles);
			for (String files : unstagedfiles) {
				System.out.println("Unstaged files" +files);
				git.add().addFilepattern(files).call();
				
				
			}

			/*
			 * // Check if the file is staged if
			 * (status.getAdded().contains("example_01.txt")) {
			 * System.out.println("File is staged for commit."); } else {
			 * System.out.println("File is not staged.");
			 * System.out.println("Untracked files:");
			 * status.getUntracked().forEach(System.out::println);
			 * System.out.println("Ignored files:"); }
			 */

			// Commit the changes
			git.commit().setMessage("Committing a file").setAuthor("Vishali Selvam", "vishaliselvam23@gmail.com")
					.call();
			System.out.println("Commit successful!");

			// Adding remote info (if it's a new repo, this needs to be done)
			System.out.println("Adding remote info");
			git.remoteAdd().setName("origin").setUri(new URIish("https://github.com/VishaliSelvam-23/Learning.git"))
					.call();

			// Push to the repository
			System.out.println("Pushing to repo");
			String token = System.getenv("GITHUB_TOKEN");
            git.push().setRemote("origin").setRefSpecs(new RefSpec("refs/heads/main")) // Ensure you're pushing to the
																						// correct branch
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider("VishaliSelvam-23",
							"token"))
					.call();
            git.close();
			System.out.println("Git Push successful!");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
