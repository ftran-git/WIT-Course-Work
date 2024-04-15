#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdlib.h>

/**
 * The main function. Note that you don't need Javadoc here,
 * although you still need document major blocks (ifs, fors, etc.)
 */
int main(int argc, char* argv[]) {
    	// checks if there are two arguments 
    	if(argc == 3) {
	   	// checks if second argument matches words
	    	if(strcmp(argv[2], "words") == 0) {
			// creating array of parameters and reference variables
			char *args[] = {"wc", "-w", argv[1], NULL};
		    	int pid = fork();
		    	int status;
			// if block to manage child/parent processes
		    	if(pid > 0) {
			    	// parent process
			    	wait(&status);
			    	printf("Child done");
			    	exit(0);
		    	} else if(pid == 0) {
			    	// child process
			    	execv("/usr/bin/wc", args);
			    	perror("error with execv");
			    	exit(-1);
		    	} else {
			    	// fork() failed
			    	printf("error with fork()");
			    	exit(1);
		   	}
		// checks if second argument matches lines
	  	} else if(strcmp(argv[2], "lines") == 0) {
			// creating array of paramters and reference variables
			char *args[] = {"wc", "-l", argv[1], NULL};
		    	int pid = fork();
		    	int status;
			// if block to manage child/parent processes
		    	if(pid > 0) {
			    	// parent process
			    	wait(&status);
			    	printf("Child done");
			    	exit(0);
		    	} else if(pid == 0) {
			    	// child process
			    	execv("/usr/bin/wc", args);
			    	perror("error with execv");
			    	exit(-1);
		    	} else {
			    	// fork() failed
			    	printf("error with fork()");
			    	exit(1);
		   	}
		// prints error message if second argument is improper  
	    	} else {
		    	printf("USAGE:\n  partb FILENAME words|lines");
		    	exit(1);
	   	}
	// prints error message if improper number of arguments is provided
    	} else {
	    printf("USAGE:\n  partb FILENAME words|lines");
	    exit(1);
    	}
    	return 0;
}
