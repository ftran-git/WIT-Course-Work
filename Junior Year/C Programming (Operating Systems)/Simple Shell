#include <stddef.h> // For NULL
#include <stdio.h> // For printf 
#include <string.h> // For strcmp
#include <stdlib.h> // For malloc 
#include <unistd.h> // For certain syscalls
#include <sys/wait.h> // For wait
		      
/**
 * Implementation of a simple shell that runs a command from the user. The program will terminate when the input is "exit".
 */
int main() {
	// used for size of line[]
	enum { MAX_LINE = 64 };
	// while loop to continue accepting input until "exit" is entered
	while(1) {
		// accepting input
		printf("$");
		char line[MAX_LINE];
		fgets(line, MAX_LINE, stdin);

		// checking for input of "exit"
		if(!strcmp(line, "exit\n")) {
			exit(0);
		}

		// seperating input using strtok()
		char delimiters[] = " \n";
		char* token = strtok(line, delimiters);

		// storing string tokens to array
		int i = 0;
		char** array = malloc(sizeof(char*) * i);
		while(token != NULL) {
			array[i] = token;
			token = strtok(NULL, delimiters);
			i++;
		}
		array[i] = NULL;

		// forking child process to run command 
		int pid = fork();
		int status;
		if(pid > 0) {
			// parent process
			wait(&status);
		} else if(pid == 0) {
			// child process
			// executing command 
			execv(array[0], array);
			perror("execv error");
		} else {
			// fork failed
			printf("Fork error\n");
			exit(1);
		}

		// freeing array when done
		free(array);
		array = NULL;
	}
	return 0;
}
