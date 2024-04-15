#include <stddef.h> // For NULL
#include <stdio.h> // For printf/fgets and others
#include <stdbool.h> // For true/false
#include <string.h>
#include <stdlib.h>
#include <unistd.h> // For certain syscalls
#include <sys/wait.h> // For wait
#include "partc.h"
#include "partc.c"

/**
 * This function is a simple command-line interface with two modes. The first mode will accept several command line arguments. The first argument will be the command to run and the remainder will be the arguments to the command. The second mode contains a colon character in part of the arguments. This character will split the arguments into two parts. The first part will run the first command and the second part will run another command. The two commands will be connected via pipe. 
 */
int main(int argc, char* argv[]) {
    	// Create two string arrays that are large enough to hold all of argv
    	char** args1 = malloc(sizeof(char*) * argc);
    	char** args2 = malloc(sizeof(char*) * argc);

    	// Checking for proper arguments
    	if(argc != 1) {
	    	// Checking for ":" to determine mode
	    	int mode = 1;
	    	for(int i = 1; i < argc; i++) {
		    	if(strcmp(argv[i], ":") == 0) {
			    	mode = 2;
		    	} 
		}
		
	    	// Block of code to handle mode 1 and 2
	    	if(mode == 1) {
		    	// mode 1
		    	// assigning arguments to args1
		    	int j = 0;
		    	for(int i = 1; i < argc; i++, j++) {
			    	args1[j] = argv[i];
		    	} 
		    	args1[j] = NULL;

		    	// block of code for child/parent processes
		    	int pid = fork();
		    	int status;
		    	if(pid > 0) {
			    	// parent process
			    	wait(&status);
			    	exit(0);
		    	} else if(pid == 0) {
			    	// child process
				// executing mode one function
			    	run_one(argv[1], args1);
		    	} else {
			    	// fork() failed
			    	printf("Fork error\n");
			    	exit(1);
		    	}
	    	} else {
		    	// mode 2
			// assigning arguments to args1 and args2 
			split_args(argc, argv, args1, args2);

		    	// block of code for child/parent processes
			int pid = fork();
			int status;
			if(pid > 0) {
				// parent process 
				wait(&status);
				exit(0);
			} else if(pid == 0) {
				// child process
				// executing mode two function
				run_pipe(args1[0], args1, args2[0], args2);
				exit(0);
			} else {
				// fork() failed
				printf("Fork error\n");
			}
	   	}
    	} else {
	    	// error message for improper arguments
	    	printf("USAGE:\n  partc CMD1 CMD1_ARGS [: CMD2 CMD2_ARGS]");
	    	exit(1);
    	}
	
    	// Free the two string arrays
    	free(args2);
    	free(args1);

    	return 0;
}
