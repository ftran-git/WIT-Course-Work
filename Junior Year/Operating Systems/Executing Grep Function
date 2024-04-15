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
	char *args[] = {"grep", "-q", argv[1], "/home/ftran/Assignments/comp3400-f22-s04-la04-tranfatwit/tests/file1.txt", NULL};
	if(argc == 2) {
		// call the system to create a new process using fork()
		int pid = fork();

		// if block for child and parent processes 
		int status;
		if(pid > 0) {
			// parent process
			wait(&status);
			if(status != 0) {
				printf("%s not found", argv[1]);
				exit(1);
			} else {
				printf("%s found", argv[1]);
				exit(0);
			}
		} else if(pid == 0) {
			// child process
			execv("/usr/bin/grep", args);
			perror("error with execv");
			exit(-1);
		} else {
			// fork() failed
			printf("error with forking");
			exit(1);
		}
	} else {
		// error if there is anything other than one argument 
		printf("USAGE:\n  parta WORD");
		exit(1);
	}
	return 0;
}
