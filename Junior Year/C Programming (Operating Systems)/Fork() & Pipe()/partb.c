#include <stddef.h> // For NULL
#include <stdio.h> // For printf/fgets and others
#include <stdbool.h> // For true/false
#include <unistd.h> // For certain syscalls
#include <sys/wait.h> // For wait
#include <stdlib.h> // For exit

/**
 * This function closes the read file descriptor and converts the write file descriptor to a file pointer. Then it reads lines from the file and writes to the pipe.
 *
 * @param read_fd
 * 	file descriptor to read
 * @param write_fd
 * 	file descriptor to write
 * @param filename
 * 	name of file
 * @returns The exit status
 */
int child_process(int read_fd, int write_fd, char filename[]) {
	// closing read file descriptor
	close(read_fd);

	// assigning to/from file pointers
	FILE *pipe_to = fdopen(write_fd, "w");
    	FILE *file_from = fopen(filename, "r");

	// block of code to read from file and write to the pipe
	enum { MAX_LINE = 64 };
	if(file_from != NULL) {
		char buffer[MAX_LINE];
		while(fgets(buffer, MAX_LINE, file_from) != NULL) {
			if(pipe_to != NULL) {
				fprintf(pipe_to, buffer);
			}
		}
	}
	fclose(file_from);
	fclose(pipe_to);
    	return 0;
}

/**
 * This function closes the write file descriptor and converts the read file descriptor into standard input. Then it will execute the "wc -l" command to count the number of lines in the file that the child process has read.
 *
 * @param read_fd
 * 	file descriptor to read
 * @param write_fd
 * 	file descriptor to write
 * @returns The exit status
 */
int parent_process(int read_fd, int write_fd) {
	// closing write file descriptor
	close(write_fd);

	// converting read file descriptor to standard input
	dup2(read_fd, STDIN_FILENO);

	// executing wc function
	char *args[] = {"wc", "-l", STDIN_FILENO, NULL};
	execv("/usr/bin/wc", args);
	perror("error with execv");
	return 0;
}

/**
 * This program generates an output in the child process and pipes it directly to the parent process where the wc command will be executed to count the number of lines in a file. 
 */
int main(int argc, char* argv[]) {
	if(argc == 2) {
		// creating pipe  
		int fdesc[2];
		int pipe_ret = pipe(fdesc);
		if(pipe_ret == -1) {
			// error message if pipe failed
			printf("Pipe error\n");
			exit(1);
		}

		// assigning read/write file descriptors
		int read_fd = fdesc[0];
		int write_fd = fdesc[1];

		// creating child process using fork 
		int pid = fork();
		int status;

		// block of code for child/parent processes
		if(pid > 0) {
			// parent process
			wait(&status);
			parent_process(read_fd, write_fd);
		} else if(pid == 0) {
			// child process
			child_process(read_fd, write_fd, argv[1]);
		} else {
			// fork() failed
			printf("Fork error\n");
			exit(1);
		}
	} else {
		// error message for improper arguments
		printf("USAGE:\n  partb FILE");
		exit(1);
	}
    	return 0;
}
