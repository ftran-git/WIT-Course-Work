#include <stddef.h> // For NULL
#include <stdio.h> // For printf/fgets and others
#include <stdbool.h> // For true/false
#include <ctype.h>
#include <stdlib.h>

/**
 * This program will copy lines from one file to another. It takes two arguments, the file to copy from, and the file to copy.
 */
int main(int argc, char* argv[]) {
	// checking for proper argument size 
	if(argc == 3) {
		enum { MAX_LINE = 64 };
		// pointers to FILE_FROM and FILE_TO
		FILE *from = fopen(argv[1], "r");
		FILE *to = fopen(argv[2], "w");
		// checking if FILE_FROM is NULL
		if(from != NULL) {
			char buffer[MAX_LINE];
			// loop to copy lines from one file to another
			while(fgets(buffer, MAX_LINE, from) != NULL) {
				// checking if FILE_TO is NULL
				if(to != NULL) {
					fprintf(to, buffer);
				}
			}
		} 
		// closing files
		fclose(from);
		fclose(to);
	} else {
		// error message for improper arguments
		printf("USAGE:\n  parta FILE_FROM FILE_TO");
		exit(1);
	}
    	return 0;
}

