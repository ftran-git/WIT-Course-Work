#include <stddef.h> // For NULL
#include <stdio.h> // For printf/fgets and others
#include <stdbool.h> // For true/false
#include <unistd.h> // For certain syscalls
#include <sys/wait.h> // For wait
#include <string.h>
#include <stdlib.h>
#include "partc.h"

/**
 * This function assigns each element from argv into args1 into it sees a ":". Then it will assign the remaining elemetns to args2 and assign NULL to the end of both.
 *
 * @param argc
 * 	number of arguments
 * @param argv
 * 	pointer to arguments
 * @param args1
 * 	stores first set of arguments
 * @param args2
 * 	stores second set of arguments
 */
void split_args(int argc, char* argv[], char* args1[], char* args2[]) {
	// variables to track if ":" has been found and where to add entries into args1 and args2
	bool found = false;
	int j = 0;

	// for loop to check each argument in argv
	for(int i = 1; i < argc; i++) {
		// checking if current argument is ":"
		if(strcmp(argv[i], ":") == 0) {
			found = true;
			// assigning NULL to last entry in args1
			args1[j] = NULL;
			j = 0;
			continue;
		}
		// if block will add to args2 if ":" has been found and to args1 if not 
		if(found) {
			args2[j] = argv[i];
			j++;
		} else { 
			args1[j] = argv[i];
			j++;
		}
	}

	// assigning NULL to last entry in args2
	args2[j] = NULL;
}

/**
 * This function takes in a command and command-line arguments to execute the given command. 
 *
 * @param command
 * 	path to command
 * @param args
 * 	arguments for command
 * @returns The exit status
 */
int run_one(const char* command, char* args[]) {
	execv(command, args);
	perror("error with excev");
	return 0;
}

/**
 * This function takes in two sets of command and commmand-line arguments and will crete a pipe and fork to execute the given commands.
 *
 * @param command1
 * 	first command path
 * @param args1
 * 	arguments for first command
 * @param command2
 * 	second command path
 * @param args2
 * 	arguments for second command
 * @returns The exit status
 */
int run_pipe(const char* command1, char* args1[], const char* command2, char* args2[]) {
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
		close(write_fd);
		dup2(read_fd, STDIN_FILENO);
		// executing second command with output from first
		execv(command2, args2);
		perror("error with execv");
		exit(-1);
	} else if(pid == 0) {
		// child process
		close(read_fd);
		dup2(write_fd, STDOUT_FILENO);
		// executing first command
		execv(command1, args1);
		perror("error with execv");
		exit(-1);
	} else {
		// fork() failed
		printf("Fork error\n");
		exit(1);
	}

	return 0;
}

